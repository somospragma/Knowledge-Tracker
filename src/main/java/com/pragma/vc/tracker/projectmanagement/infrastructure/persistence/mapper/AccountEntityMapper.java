package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.vc.tracker.projectmanagement.domain.model.Account;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountId;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountStatus;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaAccountEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper to convert between Account domain model and JPA entities
 */
public class AccountEntityMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JpaAccountEntity toEntity(Account account) {
        if (account == null) {
            return null;
        }
        JpaAccountEntity entity = new JpaAccountEntity();
        if (account.getId() != null) {
            entity.setId(account.getId().getValue());
        }
        // TODO: For now, we'll use a default regionId of 1 (Colombia)
        // This should be properly mapped from domain model when Region entity is fully integrated
        entity.setTerritoryId(1L);
        entity.setName(account.getName());
        entity.setStatus(account.getStatus().name());
        entity.setAttributes(serializeAttributes(account.getAttributes()));
        return entity;
    }

    public static Account toDomain(JpaAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountId id = entity.getId() != null ? AccountId.of(entity.getId()) : null;
        AccountStatus status = AccountStatus.valueOf(entity.getStatus());
        Map<String, String> attributes = deserializeAttributes(entity.getAttributes());

        // For now, we'll use regionId as a string for the region field
        // This maintains backward compatibility until Region is fully integrated
        String region = entity.getTerritoryId() != null ? entity.getTerritoryId().toString() : null;

        return Account.reconstitute(id, entity.getName(), region, status, attributes);
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