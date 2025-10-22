package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.*;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity.JpaKnowledgeEntity;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeEntityMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JpaKnowledgeEntity toEntity(Knowledge knowledge) {
        if (knowledge == null) return null;
        return new JpaKnowledgeEntity(
            knowledge.getId() != null ? knowledge.getId().getValue() : null,
            knowledge.getCategoryId().getValue(),
            knowledge.getName(),
            knowledge.getDescription(),
            knowledge.getApprovalStatus().name(),
            serializeAttributes(knowledge.getAttributes())
        );
    }

    public static Knowledge toDomain(JpaKnowledgeEntity entity) {
        if (entity == null) return null;
        KnowledgeId id = entity.getId() != null ? KnowledgeId.of(entity.getId()) : null;
        CategoryId categoryId = CategoryId.of(entity.getCategoryId());
        ApprovalStatus status = ApprovalStatus.valueOf(entity.getApprovalStatus());
        Map<String, String> attributes = deserializeAttributes(entity.getAttributes());

        return Knowledge.reconstitute(id, categoryId, entity.getName(),
            entity.getDescription(), status, attributes);
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
