package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.mapper;

import com.pragma.vc.tracker.peoplemanagement.domain.model.*;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity.JpaPragmaticEntity;

/**
 * Mapper to convert between Pragmatic domain model and JPA entities
 */
public class PragmaticEntityMapper {

    public static JpaPragmaticEntity toEntity(Pragmatic pragmatic) {
        if (pragmatic == null) {
            return null;
        }
        JpaPragmaticEntity entity = new JpaPragmaticEntity();
        if (pragmatic.getId() != null) {
            entity.setId(pragmatic.getId().getValue());
        }
        if (pragmatic.getChapterId() != null) {
            entity.setChapterId(pragmatic.getChapterId().getValue());
        }
        entity.setEmail(pragmatic.getEmail().getValue());
        entity.setFirstName(pragmatic.getFirstName());
        entity.setLastName(pragmatic.getLastName());
        entity.setStatus(pragmatic.getStatus().getDisplayName());
        return entity;
    }

    public static Pragmatic toDomain(JpaPragmaticEntity entity) {
        if (entity == null) {
            return null;
        }
        PragmaticId id = entity.getId() != null ? PragmaticId.of(entity.getId()) : null;
        ChapterId chapterId = entity.getChapterId() != null ? ChapterId.of(entity.getChapterId()) : null;
        Email email = Email.of(entity.getEmail());
        PragmaticStatus status = parsePragmaticStatus(entity.getStatus());

        return Pragmatic.reconstitute(
            id,
            chapterId,
            email,
            entity.getFirstName(),
            entity.getLastName(),
            status
        );
    }

    private static PragmaticStatus parsePragmaticStatus(String value) {
        if (value == null) {
            throw new IllegalArgumentException("PragmaticStatus cannot be null");
        }
        // Remove spaces and compare case-insensitively
        String normalizedValue = value.replaceAll("\\s+", "");
        for (PragmaticStatus status : PragmaticStatus.values()) {
            String normalizedEnumName = status.name().replaceAll("_", "");
            String normalizedDisplayName = status.getDisplayName().replaceAll("\\s+", "");

            if (status.name().equalsIgnoreCase(value) ||
                status.getDisplayName().equalsIgnoreCase(value) ||
                normalizedEnumName.equalsIgnoreCase(normalizedValue) ||
                normalizedDisplayName.equalsIgnoreCase(normalizedValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown PragmaticStatus: " + value);
    }
}