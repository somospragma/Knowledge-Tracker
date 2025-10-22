package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.Level;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity.JpaLevelEntity;

import java.util.HashMap;
import java.util.Map;

public class LevelEntityMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JpaLevelEntity toEntity(Level level) {
        if (level == null) return null;
        return new JpaLevelEntity(
            level.getId() != null ? level.getId().getValue() : null,
            level.getName(),
            serializeAttributes(level.getAttributes())
        );
    }

    public static Level toDomain(JpaLevelEntity entity) {
        if (entity == null) return null;
        LevelId id = entity.getId() != null ? LevelId.of(entity.getId()) : null;
        Map<String, String> attributes = deserializeAttributes(entity.getAttributes());
        return Level.reconstitute(id, entity.getName(), attributes);
    }

    private static String serializeAttributes(Map<String, String> attributes) {
        if (attributes == null || attributes.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(attributes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize attributes", e);
        }
    }

    private static Map<String, String> deserializeAttributes(String json) {
        if (json == null || json.isBlank()) return new HashMap<>();
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize attributes", e);
        }
    }
}
