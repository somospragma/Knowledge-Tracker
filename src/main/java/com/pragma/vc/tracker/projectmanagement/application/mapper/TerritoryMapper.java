package com.pragma.vc.tracker.projectmanagement.application.mapper;

import com.pragma.vc.tracker.projectmanagement.application.dto.TerritoryDTO;
import com.pragma.vc.tracker.projectmanagement.domain.model.Territory;

/**
 * Mapper to convert between Territory domain model and DTOs
 */
public class TerritoryMapper {

    public static TerritoryDTO toDTO(Territory territory) {
        if (territory == null) {
            return null;
        }
        return new TerritoryDTO(
            territory.getId() != null ? territory.getId().getValue() : null,
            territory.getName()
        );
    }
}
