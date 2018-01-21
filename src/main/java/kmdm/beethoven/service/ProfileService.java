package kmdm.beethoven.service;

import kmdm.beethoven.service.dto.ProfileDTO;
import java.util.List;

/**
 * Service Interface for managing Profile.
 */
public interface ProfileService {

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save
     * @return the persisted entity
     */
    ProfileDTO save(ProfileDTO profileDTO);

    /**
     * Get all the profiles.
     *
     * @return the list of entities
     */
    List<ProfileDTO> findAll();

    /**
     * Get the "id" profile.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ProfileDTO findOne(Long id);

    /**
     * Delete the "id" profile.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
