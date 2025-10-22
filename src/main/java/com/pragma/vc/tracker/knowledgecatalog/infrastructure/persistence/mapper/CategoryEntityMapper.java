package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.mapper;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.Category;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.CategoryId;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity.JpaCategoryEntity;

public class CategoryEntityMapper {

    public static JpaCategoryEntity toEntity(Category category) {
        if (category == null) return null;
        return new JpaCategoryEntity(
            category.getId() != null ? category.getId().getValue() : null,
            category.getName()
        );
    }

    public static Category toDomain(JpaCategoryEntity entity) {
        if (entity == null) return null;
        CategoryId id = entity.getId() != null ? CategoryId.of(entity.getId()) : null;
        return Category.reconstitute(id, entity.getName());
    }
}
