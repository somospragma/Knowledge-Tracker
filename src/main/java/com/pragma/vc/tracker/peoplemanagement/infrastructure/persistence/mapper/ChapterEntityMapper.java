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
        JpaChapterEntity entity = new JpaChapterEntity();
        if (chapter.getId() != null) {
            entity.setId(chapter.getId().getValue());
        }
        // TODO: For now, we'll use a default kcId of 1
        // This should be properly mapped from domain model when KC-Team entity is fully integrated
        entity.setKcId(1L);
        entity.setName(chapter.getName());
        entity.setStatus("Active");
        return entity;
    }

    public static Chapter toDomain(JpaChapterEntity entity) {
        if (entity == null) {
            return null;
        }
        ChapterId id = entity.getId() != null ? ChapterId.of(entity.getId()) : null;
        return Chapter.reconstitute(id, entity.getName());
    }
}