package com.pragma.vc.tracker.peoplemanagement.application.mapper;

import com.pragma.vc.tracker.peoplemanagement.application.dto.ChapterDTO;
import com.pragma.vc.tracker.peoplemanagement.domain.model.Chapter;

/**
 * Mapper to convert between Chapter domain model and DTOs
 */
public class ChapterMapper {

    public static ChapterDTO toDTO(Chapter chapter) {
        if (chapter == null) {
            return null;
        }
        return new ChapterDTO(
            chapter.getId() != null ? chapter.getId().getValue() : null,
            chapter.getName()
        );
    }
}