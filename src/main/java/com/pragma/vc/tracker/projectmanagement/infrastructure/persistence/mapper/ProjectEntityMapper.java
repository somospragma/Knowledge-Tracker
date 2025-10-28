package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.vc.tracker.projectmanagement.domain.model.*;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaProjectEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper to convert between Project domain model and JPA entities
 */
public class ProjectEntityMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JpaProjectEntity toEntity(Project project) {
        if (project == null) {
            return null;
        }
        JpaProjectEntity entity = new JpaProjectEntity();
        if (project.getId() != null) {
            entity.setId(project.getId().getValue());
        }
        entity.setAccountId(project.getAccountId().getValue());
        entity.setName(project.getName());
        entity.setStatus(project.getStatus().name());
        if (project.getDateRange() != null) {
            entity.setStartDate(project.getDateRange().getStartDate());
            entity.setEndDate(project.getDateRange().getEndDate());
        }
        entity.setType(project.getType().name());
        entity.setAttributes(serializeAttributes(project.getAttributes()));
        return entity;
    }

    public static Project toDomain(JpaProjectEntity entity) {
        if (entity == null) {
            return null;
        }
        ProjectId id = entity.getId() != null ? ProjectId.of(entity.getId()) : null;
        AccountId accountId = AccountId.of(entity.getAccountId());
        ProjectStatus status = ProjectStatus.valueOf(entity.getStatus());
        ProjectType type = ProjectType.valueOf(entity.getType());
        DateRange dateRange = null;
        if (entity.getStartDate() != null) {
            dateRange = DateRange.of(entity.getStartDate(), entity.getEndDate());
        }
        Map<String, String> attributes = deserializeAttributes(entity.getAttributes());

        return Project.reconstitute(id, accountId, entity.getName(), status, dateRange, type, attributes);
    }

    private static String serializeAttributes(Map<String, String> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attributes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize attributes", e);
        }
    }

    private static Map<String, String> deserializeAttributes(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize attributes", e);
        }
    }
}