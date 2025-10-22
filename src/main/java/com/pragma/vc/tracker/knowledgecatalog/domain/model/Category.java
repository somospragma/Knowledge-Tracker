package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import java.util.Objects;

/**
 * Category Entity - Aggregate Root
 * Represents a knowledge category (Platform, Language, Framework, Tool, Technique)
 *
 * Domain rules:
 * - Category name must be unique
 * - Category name cannot be blank
 */
public class Category {
    private CategoryId id;
    private String name;

    // Private constructor to enforce factory method usage
    private Category(CategoryId id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to create a new Category
     */
    public static Category create(String name) {
        return new Category(null, name);
    }

    /**
     * Factory method to reconstitute a Category from persistence
     */
    public static Category reconstitute(CategoryId id, String name) {
        return new Category(id, name);
    }

    // Business methods

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    // Validation methods

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null or blank");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Category name cannot exceed 50 characters");
        }
    }

    // Getters

    public CategoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // For infrastructure layer to set ID after persistence
    public void setId(CategoryId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Category ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}