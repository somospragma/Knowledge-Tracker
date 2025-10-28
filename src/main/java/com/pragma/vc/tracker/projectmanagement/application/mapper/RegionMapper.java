package com.pragma.vc.tracker.projectmanagement.application.mapper;

import com.pragma.vc.tracker.projectmanagement.application.dto.RegionDTO;
import com.pragma.vc.tracker.projectmanagement.domain.model.Region;

/**
 * Mapper to convert between Region domain model and DTOs
 */
public class RegionMapper {

    public static RegionDTO toDTO(Region region) {
        if (region == null) {
            return null;
        }
        return new RegionDTO(
            region.getId() != null ? region.getId().getValue() : null,
            region.getName()
        );
    }
}
