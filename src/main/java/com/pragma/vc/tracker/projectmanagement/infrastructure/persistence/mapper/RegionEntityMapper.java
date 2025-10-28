package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper;

import com.pragma.vc.tracker.projectmanagement.domain.model.Region;
import com.pragma.vc.tracker.projectmanagement.domain.model.RegionId;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaRegionEntity;

/**
 * Mapper to convert between Region domain model and JPA entities
 */
public class RegionEntityMapper {

    public static JpaRegionEntity toEntity(Region region) {
        if (region == null) {
            return null;
        }
        JpaRegionEntity entity = new JpaRegionEntity();
        if (region.getId() != null) {
            entity.setId(region.getId().getValue());
        }
        entity.setName(region.getName());
        return entity;
    }

    public static Region toDomain(JpaRegionEntity entity) {
        if (entity == null) {
            return null;
        }
        RegionId id = entity.getId() != null ? RegionId.of(entity.getId()) : null;
        return Region.reconstitute(id, entity.getName());
    }
}
