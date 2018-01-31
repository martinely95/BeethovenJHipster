package kmdm.beethoven.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import kmdm.beethoven.domain.MelodyEntity;
import kmdm.beethoven.repository.MelodyEntityRepository;
import kmdm.beethoven.service.MelodyEntityService;
import kmdm.beethoven.service.UserService;
import kmdm.beethoven.service.dto.MelodyEntityDTO;
import kmdm.beethoven.web.rest.errors.BadRequestAlertException;
import kmdm.beethoven.web.rest.util.HeaderUtil;
import kmdm.beethoven.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MelodyEntity.
 */
@RestController
@RequestMapping("/api")
public class MelodyEntityResource {

    private final Logger log = LoggerFactory.getLogger(MelodyEntityResource.class);

    private static final String ENTITY_NAME = "melodyEntity";

    private final MelodyEntityService melodyEntityService;

    @Inject
    private MelodyEntityRepository melodyEntityRepository;

    public MelodyEntityResource(MelodyEntityService melodyEntityService) {
        this.melodyEntityService = melodyEntityService;
    }

    /**
     * POST  /melody-entities : Create a new melodyEntity.
     *
     * @param melodyEntityDTO the melodyEntityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new melodyEntityDTO, or with status 400 (Bad Request) if the melodyEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/melody-entities")
    @Timed
    public ResponseEntity<MelodyEntityDTO> createMelodyEntity(@Valid @RequestBody MelodyEntityDTO melodyEntityDTO) throws URISyntaxException {
        log.debug("REST request to save MelodyEntity : {}", melodyEntityDTO);
        if (melodyEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new melodyEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MelodyEntityDTO result = melodyEntityService.save(melodyEntityDTO);
        return ResponseEntity.created(new URI("/api/melody-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /melody-entities : Updates an existing melodyEntity.
     *
     * @param melodyEntityDTO the melodyEntityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated melodyEntityDTO,
     * or with status 400 (Bad Request) if the melodyEntityDTO is not valid,
     * or with status 500 (Internal Server Error) if the melodyEntityDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/melody-entities")
    @Timed
    public ResponseEntity<MelodyEntityDTO> updateMelodyEntity(@Valid @RequestBody MelodyEntityDTO melodyEntityDTO) throws URISyntaxException {
        log.debug("REST request to update MelodyEntity : {}", melodyEntityDTO);
        if (melodyEntityDTO.getId() == null) {
            return createMelodyEntity(melodyEntityDTO);
        }
        MelodyEntityDTO result = melodyEntityService.save(melodyEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, melodyEntityDTO.getId().toString()))
            .body(result);
    }

    @Inject
    UserService userService;

    /**
     * GET  /melody-entities : get all the melodyEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of melodyEntities in body
     */
    @GetMapping("/melody-entities")
    @Timed
    public ResponseEntity<List<MelodyEntity>> getAllMelodyEntities(Pageable pageable) throws Exception {
        log.debug("REST request to get a page of MelodyEntities");

        Page<MelodyEntity> page = melodyEntityRepository.findAllByProfileId(pageable, userService.getCurrentlyLoggedInProfile().getId());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/melody-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /melody-entities/:id : get the "id" melodyEntity.
     *
     * @param id the id of the melodyEntityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the melodyEntityDTO, or with status 404 (Not Found)
     */
    @GetMapping("/melody-entities/{id}")
    @Timed
    public ResponseEntity<MelodyEntityDTO> getMelodyEntity(@PathVariable Long id) {
        log.debug("REST request to get MelodyEntity : {}", id);
        MelodyEntityDTO melodyEntityDTO = melodyEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(melodyEntityDTO));
    }

    /**
     * DELETE  /melody-entities/:id : delete the "id" melodyEntity.
     *
     * @param id the id of the melodyEntityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/melody-entities/{id}")
    @Timed
    public ResponseEntity<Void> deleteMelodyEntity(@PathVariable Long id) {
        log.debug("REST request to delete MelodyEntity : {}", id);
        melodyEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
