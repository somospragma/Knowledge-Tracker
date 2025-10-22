package com.pragma.vc.tracker.knowledgecatalog.application.mapper;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.LevelDTO;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.Level;

public class LevelMapper {

    public static LevelDTO toDTO(Level level) {
        if (level == null) {
            return null;
        }
        return new LevelDTO(
            level.getId() != null ? level.getId().getValue() : null,
            level.getName(),
            level.getAttributes()
        );
    }
}