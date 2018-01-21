package kmdm.beethoven.web.rest;

import kmdm.beethoven.BeethovenApp;

import kmdm.beethoven.domain.MelodyEntity;
import kmdm.beethoven.repository.MelodyEntityRepository;
import kmdm.beethoven.service.MelodyEntityService;
import kmdm.beethoven.service.dto.MelodyEntityDTO;
import kmdm.beethoven.service.mapper.MelodyEntityMapper;
import kmdm.beethoven.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static kmdm.beethoven.web.rest.TestUtil.sameInstant;
import static kmdm.beethoven.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MelodyEntityResource REST controller.
 *
 * @see MelodyEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeethovenApp.class)
public class MelodyEntityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MelodyEntityRepository melodyEntityRepository;

    @Autowired
    private MelodyEntityMapper melodyEntityMapper;

    @Autowired
    private MelodyEntityService melodyEntityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMelodyEntityMockMvc;

    private MelodyEntity melodyEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MelodyEntityResource melodyEntityResource = new MelodyEntityResource(melodyEntityService);
        this.restMelodyEntityMockMvc = MockMvcBuilders.standaloneSetup(melodyEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MelodyEntity createEntity(EntityManager em) {
        MelodyEntity melodyEntity = new MelodyEntity()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .createdDateTime(DEFAULT_CREATED_DATE_TIME);
        return melodyEntity;
    }

    @Before
    public void initTest() {
        melodyEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createMelodyEntity() throws Exception {
        int databaseSizeBeforeCreate = melodyEntityRepository.findAll().size();

        // Create the MelodyEntity
        MelodyEntityDTO melodyEntityDTO = melodyEntityMapper.toDto(melodyEntity);
        restMelodyEntityMockMvc.perform(post("/api/melody-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(melodyEntityDTO)))
            .andExpect(status().isCreated());

        // Validate the MelodyEntity in the database
        List<MelodyEntity> melodyEntityList = melodyEntityRepository.findAll();
        assertThat(melodyEntityList).hasSize(databaseSizeBeforeCreate + 1);
        MelodyEntity testMelodyEntity = melodyEntityList.get(melodyEntityList.size() - 1);
        assertThat(testMelodyEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMelodyEntity.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMelodyEntity.getCreatedDateTime()).isEqualTo(DEFAULT_CREATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void createMelodyEntityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = melodyEntityRepository.findAll().size();

        // Create the MelodyEntity with an existing ID
        melodyEntity.setId(1L);
        MelodyEntityDTO melodyEntityDTO = melodyEntityMapper.toDto(melodyEntity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMelodyEntityMockMvc.perform(post("/api/melody-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(melodyEntityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MelodyEntity in the database
        List<MelodyEntity> melodyEntityList = melodyEntityRepository.findAll();
        assertThat(melodyEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = melodyEntityRepository.findAll().size();
        // set the field null
        melodyEntity.setName(null);

        // Create the MelodyEntity, which fails.
        MelodyEntityDTO melodyEntityDTO = melodyEntityMapper.toDto(melodyEntity);

        restMelodyEntityMockMvc.perform(post("/api/melody-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(melodyEntityDTO)))
            .andExpect(status().isBadRequest());

        List<MelodyEntity> melodyEntityList = melodyEntityRepository.findAll();
        assertThat(melodyEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = melodyEntityRepository.findAll().size();
        // set the field null
        melodyEntity.setCreatedDateTime(null);

        // Create the MelodyEntity, which fails.
        MelodyEntityDTO melodyEntityDTO = melodyEntityMapper.toDto(melodyEntity);

        restMelodyEntityMockMvc.perform(post("/api/melody-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(melodyEntityDTO)))
            .andExpect(status().isBadRequest());

        List<MelodyEntity> melodyEntityList = melodyEntityRepository.findAll();
        assertThat(melodyEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMelodyEntities() throws Exception {
        // Initialize the database
        melodyEntityRepository.saveAndFlush(melodyEntity);

        // Get all the melodyEntityList
        restMelodyEntityMockMvc.perform(get("/api/melody-entities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(melodyEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdDateTime").value(hasItem(sameInstant(DEFAULT_CREATED_DATE_TIME))));
    }

    @Test
    @Transactional
    public void getMelodyEntity() throws Exception {
        // Initialize the database
        melodyEntityRepository.saveAndFlush(melodyEntity);

        // Get the melodyEntity
        restMelodyEntityMockMvc.perform(get("/api/melody-entities/{id}", melodyEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(melodyEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdDateTime").value(sameInstant(DEFAULT_CREATED_DATE_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingMelodyEntity() throws Exception {
        // Get the melodyEntity
        restMelodyEntityMockMvc.perform(get("/api/melody-entities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMelodyEntity() throws Exception {
        // Initialize the database
        melodyEntityRepository.saveAndFlush(melodyEntity);
        int databaseSizeBeforeUpdate = melodyEntityRepository.findAll().size();

        // Update the melodyEntity
        MelodyEntity updatedMelodyEntity = melodyEntityRepository.findOne(melodyEntity.getId());
        // Disconnect from session so that the updates on updatedMelodyEntity are not directly saved in db
        em.detach(updatedMelodyEntity);
        updatedMelodyEntity
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .createdDateTime(UPDATED_CREATED_DATE_TIME);
        MelodyEntityDTO melodyEntityDTO = melodyEntityMapper.toDto(updatedMelodyEntity);

        restMelodyEntityMockMvc.perform(put("/api/melody-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(melodyEntityDTO)))
            .andExpect(status().isOk());

        // Validate the MelodyEntity in the database
        List<MelodyEntity> melodyEntityList = melodyEntityRepository.findAll();
        assertThat(melodyEntityList).hasSize(databaseSizeBeforeUpdate);
        MelodyEntity testMelodyEntity = melodyEntityList.get(melodyEntityList.size() - 1);
        assertThat(testMelodyEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMelodyEntity.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMelodyEntity.getCreatedDateTime()).isEqualTo(UPDATED_CREATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingMelodyEntity() throws Exception {
        int databaseSizeBeforeUpdate = melodyEntityRepository.findAll().size();

        // Create the MelodyEntity
        MelodyEntityDTO melodyEntityDTO = melodyEntityMapper.toDto(melodyEntity);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMelodyEntityMockMvc.perform(put("/api/melody-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(melodyEntityDTO)))
            .andExpect(status().isCreated());

        // Validate the MelodyEntity in the database
        List<MelodyEntity> melodyEntityList = melodyEntityRepository.findAll();
        assertThat(melodyEntityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMelodyEntity() throws Exception {
        // Initialize the database
        melodyEntityRepository.saveAndFlush(melodyEntity);
        int databaseSizeBeforeDelete = melodyEntityRepository.findAll().size();

        // Get the melodyEntity
        restMelodyEntityMockMvc.perform(delete("/api/melody-entities/{id}", melodyEntity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MelodyEntity> melodyEntityList = melodyEntityRepository.findAll();
        assertThat(melodyEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MelodyEntity.class);
        MelodyEntity melodyEntity1 = new MelodyEntity();
        melodyEntity1.setId(1L);
        MelodyEntity melodyEntity2 = new MelodyEntity();
        melodyEntity2.setId(melodyEntity1.getId());
        assertThat(melodyEntity1).isEqualTo(melodyEntity2);
        melodyEntity2.setId(2L);
        assertThat(melodyEntity1).isNotEqualTo(melodyEntity2);
        melodyEntity1.setId(null);
        assertThat(melodyEntity1).isNotEqualTo(melodyEntity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MelodyEntityDTO.class);
        MelodyEntityDTO melodyEntityDTO1 = new MelodyEntityDTO();
        melodyEntityDTO1.setId(1L);
        MelodyEntityDTO melodyEntityDTO2 = new MelodyEntityDTO();
        assertThat(melodyEntityDTO1).isNotEqualTo(melodyEntityDTO2);
        melodyEntityDTO2.setId(melodyEntityDTO1.getId());
        assertThat(melodyEntityDTO1).isEqualTo(melodyEntityDTO2);
        melodyEntityDTO2.setId(2L);
        assertThat(melodyEntityDTO1).isNotEqualTo(melodyEntityDTO2);
        melodyEntityDTO1.setId(null);
        assertThat(melodyEntityDTO1).isNotEqualTo(melodyEntityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(melodyEntityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(melodyEntityMapper.fromId(null)).isNull();
    }
}
