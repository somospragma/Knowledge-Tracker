package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Level Entity - Aggregate Root
 * Represents a proficiency level (Beginner, Intermediate, Advanced, Expert)
 *
 * Domain rules:
 * - Level name must be unique
 * - Level name cannot be blank
 * - Attributes store additional metadata
 */
public class Level {
    private LevelId id;
    private String name;
    private Map<String, String> attributes;

    // Private constructor to enforce factory method usage
    private Level(LevelId id, String name, Map<String, String> attributes) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.attributes = new HashMap<>(attributes != null ? attributes : new HashMap<>());
    }

    /**
     * Factory method to create a new Level
     */
    public static Level create(String name, Map<String, String> attributes) {
        return new Level(null, name, attributes);
    }

    /**
     * Factory method to reconstitute a Level from persistence
     */
    public static Level reconstitute(LevelId id, String name, Map<String, String> attributes) {
        return new Level(id, name, attributes);
    }

    // Business methods

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void addAttribute(String key, String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Attribute key cannot be null or blank");
        }
        this.attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    // Validation methods

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Level name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Level name cannot exceed 255 characters");
        }
    }

    // Getters

    public LevelId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes); // Return defensive copy
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    // For infrastructure layer to set ID after persistence
    public void setId(LevelId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Level ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return Objects.equals(id, level.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Level{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}