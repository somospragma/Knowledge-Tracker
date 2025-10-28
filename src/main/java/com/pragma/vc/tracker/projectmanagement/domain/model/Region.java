package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.Objects;

/**
 * Region Entity - Aggregate Root
 * Represents a geographical region where accounts operate
 *
 * Domain rules:
 * - Region name must be unique and not blank
 * - Region name cannot exceed 255 characters
 */
public class Region {
    private RegionId id;
    private String name;

    // Private constructor to enforce factory method usage
    private Region(RegionId id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to create a new Region
     */
    public static Region create(String name) {
        return new Region(null, name);
    }

    /**
     * Factory method to reconstitute a Region from persistence
     */
    public static Region reconstitute(RegionId id, String name) {
        return new Region(id, name);
    }

    // Business methods

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    // Validation methods

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Region name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Region name cannot exceed 255 characters");
        }
    }

    // Getters

    public RegionId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // For infrastructure layer to set ID after persistence
    public void setId(RegionId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Region ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(id, region.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Region{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
