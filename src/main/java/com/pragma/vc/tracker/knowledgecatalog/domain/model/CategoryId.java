package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import java.util.Objects;

/**
 * Value Object representing Category identifier
 * Immutable and self-validating
 */
public class CategoryId {
    private final Long value;

    private CategoryId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Category ID must be a positive number");
        }
        this.value = value;
    }

    public static CategoryId of(Long value) {
        return new CategoryId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId that = (CategoryId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "CategoryId{" + value + "}";
    }
}