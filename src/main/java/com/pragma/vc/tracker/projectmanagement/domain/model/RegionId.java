package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.Objects;

/**
 * Value Object representing a Region identifier
 * Immutable and type-safe
 */
public class RegionId {
    private final Long value;

    private RegionId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Region ID must be a positive number");
        }
        this.value = value;
    }

    public static RegionId of(Long value) {
        return new RegionId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionId regionId = (RegionId) o;
        return Objects.equals(value, regionId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "RegionId{" + value + '}';
    }
}
