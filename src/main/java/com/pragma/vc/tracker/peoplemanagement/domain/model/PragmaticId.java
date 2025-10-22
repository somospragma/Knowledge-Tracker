package com.pragma.vc.tracker.peoplemanagement.domain.model;

import java.util.Objects;

/**
 * Value Object representing Pragmatic identifier
 * Immutable and self-validating
 */
public class PragmaticId {
    private final Long value;

    private PragmaticId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Pragmatic ID must be a positive number");
        }
        this.value = value;
    }

    public static PragmaticId of(Long value) {
        return new PragmaticId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PragmaticId that = (PragmaticId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PragmaticId{" + value + "}";
    }
}