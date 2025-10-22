package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.mapper;

import com.pragma.vc.tracker.peoplemanagement.domain.model.Chapter;
import com.pragma.vc.tracker.peoplemanagement.domain.model.ChapterId;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity.JpaChapterEntity;

/**
 * Mapper to convert between Chapter domain model and JPA entities
 */
public class ChapterEntityMapper {

    public static JpaChapterEntity toEntity(Chapter chapter) {
        if (chapter == null) {
            return null;
        }
        return new JpaChapterEntity(
            chapter.getId() != null ? chapter.getId().getValue() : null,
            chapter.getName()
        );
    }

    public static Chapter toDomain(JpaChapterEntity entity) {
        if (entity == null) {
            return null;
        }
        ChapterId id = entity.getId() != null ? ChapterId.of(entity.getId()) : null;
        return Chapter.reconstitute(id, entity.getName());
    }
}