package com.pragma.vc.tracker.peoplemanagement.domain.model;

import java.util.Objects;

/**
 * Value Object representing Chapter identifier
 * Immutable and self-validating
 */
public class ChapterId {
    private final Long value;

    private ChapterId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Chapter ID must be a positive number");
        }
        this.value = value;
    }

    public static ChapterId of(Long value) {
        return new ChapterId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterId chapterId = (ChapterId) o;
        return Objects.equals(value, chapterId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ChapterId{" + value + "}";
    }
}
