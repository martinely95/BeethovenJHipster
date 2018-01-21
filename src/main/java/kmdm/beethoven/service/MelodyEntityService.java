package kmdm.beethoven.service;

import kmdm.beethoven.service.dto.MelodyEntityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MelodyEntity.
 */
public interface MelodyEntityService {

    /**
     * Save a melodyEntity.
     *
     * @param melodyEntityDTO the entity to save
     * @return the persisted entity
     */
    MelodyEntityDTO save(MelodyEntityDTO melodyEntityDTO);

    /**
     * Get all the melodyEntities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MelodyEntityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" melodyEntity.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MelodyEntityDTO findOne(Long id);

    /**
     * Delete the "id" melodyEntity.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
