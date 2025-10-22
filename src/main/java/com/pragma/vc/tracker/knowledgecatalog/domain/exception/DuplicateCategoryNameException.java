package com.pragma.vc.tracker.knowledgecatalog.domain.exception;

/**
 * Domain exception thrown when attempting to create a Category with a duplicate name
 */
public class DuplicateCategoryNameException extends RuntimeException {
    private final String categoryName;

    public DuplicateCategoryNameException(String categoryName) {
        super("Category already exists with name: " + categoryName);
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}