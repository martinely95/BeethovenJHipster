package kmdm.beethoven.repository;

import kmdm.beethoven.domain.MelodyEntity;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MelodyEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MelodyEntityRepository extends JpaRepository<MelodyEntity, Long> {

}
