package com.pragma.vc.tracker.knowledgecatalog.domain.exception;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.CategoryId;

/**
 * Domain exception thrown when a Category is not found
 */
public class CategoryNotFoundException extends RuntimeException {
    private final CategoryId categoryId;

    public CategoryNotFoundException(CategoryId categoryId) {
        super("Category not found with id: " + categoryId);
        this.categoryId = categoryId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }
}