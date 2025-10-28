package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.mapper;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.Category;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.CategoryId;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity.JpaCategoryEntity;

public class CategoryEntityMapper {

    public static JpaCategoryEntity toEntity(Category category) {
        if (category == null) return null;
        JpaCategoryEntity entity = new JpaCategoryEntity();
        if (category.getId() != null) {
            entity.setId(category.getId().getValue());
        }
        entity.setName(category.getName());
        return entity;
    }

    public static Category toDomain(JpaCategoryEntity entity) {
        if (entity == null) return null;
        CategoryId id = entity.getId() != null ? CategoryId.of(entity.getId()) : null;
        return Category.reconstitute(id, entity.getName());
    }
}
