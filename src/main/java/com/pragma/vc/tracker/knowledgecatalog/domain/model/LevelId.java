package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import java.util.Objects;

/**
 * Value Object representing Level identifier
 * Immutable and self-validating
 */
public class LevelId {
    private final Long value;

    private LevelId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Level ID must be a positive number");
        }
        this.value = value;
    }

    public static LevelId of(Long value) {
        return new LevelId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelId levelId = (LevelId) o;
        return Objects.equals(value, levelId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "LevelId{" + value + "}";
    }
}