package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.Objects;

/**
 * Value Object representing a Territory identifier
 * Immutable and type-safe
 */
public class TerritoryId {
    private final Long value;

    private TerritoryId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Territory ID must be a positive number");
        }
        this.value = value;
    }

    public static TerritoryId of(Long value) {
        return new TerritoryId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerritoryId territoryId = (TerritoryId) o;
        return Objects.equals(value, territoryId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TerritoryId{" + value + '}';
    }
}
