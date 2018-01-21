package kmdm.beethoven.service.mapper;

import kmdm.beethoven.domain.*;
import kmdm.beethoven.service.dto.MelodyEntityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MelodyEntity and its DTO MelodyEntityDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface MelodyEntityMapper extends EntityMapper<MelodyEntityDTO, MelodyEntity> {

    @Mapping(source = "profile.id", target = "profileId")
    MelodyEntityDTO toDto(MelodyEntity melodyEntity);

    @Mapping(source = "profileId", target = "profile")
    MelodyEntity toEntity(MelodyEntityDTO melodyEntityDTO);

    default MelodyEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        MelodyEntity melodyEntity = new MelodyEntity();
        melodyEntity.setId(id);
        return melodyEntity;
    }
}
