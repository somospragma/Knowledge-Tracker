package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper;

import com.pragma.vc.tracker.projectmanagement.domain.model.Territory;
import com.pragma.vc.tracker.projectmanagement.domain.model.TerritoryId;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaTerritoryEntity;

/**
 * Mapper to convert between Territory domain model and JPA entities
 */
public class TerritoryEntityMapper {

    public static JpaTerritoryEntity toEntity(Territory territory) {
        if (territory == null) {
            return null;
        }
        JpaTerritoryEntity entity = new JpaTerritoryEntity();
        if (territory.getId() != null) {
            entity.setId(territory.getId().getValue());
        }
        entity.setName(territory.getName());
        return entity;
    }

    public static Territory toDomain(JpaTerritoryEntity entity) {
        if (entity == null) {
            return null;
        }
        TerritoryId id = entity.getId() != null ? TerritoryId.of(entity.getId()) : null;
        return Territory.reconstitute(id, entity.getName());
    }
}
