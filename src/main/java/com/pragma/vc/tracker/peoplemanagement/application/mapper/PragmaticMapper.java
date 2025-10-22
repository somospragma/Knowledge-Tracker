package com.pragma.vc.tracker.peoplemanagement.application.mapper;

import com.pragma.vc.tracker.peoplemanagement.application.dto.PragmaticDTO;
import com.pragma.vc.tracker.peoplemanagement.domain.model.Pragmatic;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticStatus;

/**
 * Mapper to convert between Pragmatic domain model and DTOs
 */
public class PragmaticMapper {

    public static PragmaticDTO toDTO(Pragmatic pragmatic) {
        return toDTO(pragmatic, null);
    }

    public static PragmaticDTO toDTO(Pragmatic pragmatic, String chapterName) {
        if (pragmatic == null) {
            return null;
        }
        return new PragmaticDTO(
            pragmatic.getId() != null ? pragmatic.getId().getValue() : null,
            pragmatic.getChapterId() != null ? pragmatic.getChapterId().getValue() : null,
            chapterName,
            pragmatic.getEmail().getValue(),
            pragmatic.getFirstName(),
            pragmatic.getLastName(),
            pragmatic.getFullName(),
            pragmatic.getStatus().name()
        );
    }

    public static PragmaticStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return PragmaticStatus.ACTIVE;
        }
        try {
            return PragmaticStatus.valueOf(status.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid pragmatic status: " + status);
        }
    }
}