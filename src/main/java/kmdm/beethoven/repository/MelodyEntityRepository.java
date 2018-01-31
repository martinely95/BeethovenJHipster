package kmdm.beethoven.repository;

import kmdm.beethoven.domain.MelodyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * Spring Data JPA repository for the MelodyEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MelodyEntityRepository extends JpaRepository<MelodyEntity, Long> {

    Page<MelodyEntity> findAllByProfileId(Pageable pageable, Long profileId);

}
