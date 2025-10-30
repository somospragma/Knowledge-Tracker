package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.Objects;

/**
 * Territory Entity - Aggregate Root
 * Represents a geographical territory where accounts operate
 *
 * Domain rules:
 * - Territory name must be unique and not blank
 * - Territory name cannot exceed 255 characters
 */
public class Territory {
    private TerritoryId id;
    private String name;

    // Private constructor to enforce factory method usage
    private Territory(TerritoryId id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to create a new Territory
     */
    public static Territory create(String name) {
        return new Territory(null, name);
    }

    /**
     * Factory method to reconstitute a Territory from persistence
     */
    public static Territory reconstitute(TerritoryId id, String name) {
        return new Territory(id, name);
    }

    // Business methods

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    // Validation methods

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Territory name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Territory name cannot exceed 255 characters");
        }
    }

    // Getters

    public TerritoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // For infrastructure layer to set ID after persistence
    public void setId(TerritoryId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Territory ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Territory territory = (Territory) o;
        return Objects.equals(id, territory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Territory{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
