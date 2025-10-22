package com.pragma.vc.tracker.knowledgecatalog.application.mapper;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CategoryDTO;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.Category;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(
            category.getId() != null ? category.getId().getValue() : null,
            category.getName()
        );
    }
}