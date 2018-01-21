package kmdm.beethoven.service.impl;

import kmdm.beethoven.service.MelodyEntityService;
import kmdm.beethoven.domain.MelodyEntity;
import kmdm.beethoven.repository.MelodyEntityRepository;
import kmdm.beethoven.service.dto.MelodyEntityDTO;
import kmdm.beethoven.service.mapper.MelodyEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MelodyEntity.
 */
@Service
@Transactional
public class MelodyEntityServiceImpl implements MelodyEntityService {

    private final Logger log = LoggerFactory.getLogger(MelodyEntityServiceImpl.class);

    private final MelodyEntityRepository melodyEntityRepository;

    private final MelodyEntityMapper melodyEntityMapper;

    public MelodyEntityServiceImpl(MelodyEntityRepository melodyEntityRepository, MelodyEntityMapper melodyEntityMapper) {
        this.melodyEntityRepository = melodyEntityRepository;
        this.melodyEntityMapper = melodyEntityMapper;
    }

    /**
     * Save a melodyEntity.
     *
     * @param melodyEntityDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MelodyEntityDTO save(MelodyEntityDTO melodyEntityDTO) {
        log.debug("Request to save MelodyEntity : {}", melodyEntityDTO);
        MelodyEntity melodyEntity = melodyEntityMapper.toEntity(melodyEntityDTO);
        melodyEntity = melodyEntityRepository.save(melodyEntity);
        return melodyEntityMapper.toDto(melodyEntity);
    }

    /**
     * Get all the melodyEntities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MelodyEntityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MelodyEntities");
        return melodyEntityRepository.findAll(pageable)
            .map(melodyEntityMapper::toDto);
    }

    /**
     * Get one melodyEntity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MelodyEntityDTO findOne(Long id) {
        log.debug("Request to get MelodyEntity : {}", id);
        MelodyEntity melodyEntity = melodyEntityRepository.findOne(id);
        return melodyEntityMapper.toDto(melodyEntity);
    }

    /**
     * Delete the melodyEntity by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MelodyEntity : {}", id);
        melodyEntityRepository.delete(id);
    }
}
