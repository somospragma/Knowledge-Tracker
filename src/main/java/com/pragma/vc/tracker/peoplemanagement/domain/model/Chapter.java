package com.pragma.vc.tracker.peoplemanagement.domain.model;

import java.util.Objects;

/**
 * Chapter Entity - Aggregate Root
 * Represents an organizational unit (chapter) within Pragma SA
 *
 * Domain rules:
 * - Chapter name must be unique and not blank
 * - Chapter name represents the technical specialty (e.g., Backend, Frontend, DevOps)
 */
public class Chapter {
    private ChapterId id;
    private String name;

    // Private constructor to enforce factory method usage
    private Chapter(ChapterId id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to create a new Chapter
     */
    public static Chapter create(String name) {
        return new Chapter(null, name);
    }

    /**
     * Factory method to reconstitute a Chapter from persistence
     */
    public static Chapter reconstitute(ChapterId id, String name) {
        return new Chapter(id, name);
    }

    // Business methods

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    // Validation methods

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Chapter name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Chapter name cannot exceed 255 characters");
        }
    }

    // Getters

    public ChapterId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // For infrastructure layer to set ID after persistence
    public void setId(ChapterId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Chapter ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return Objects.equals(id, chapter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Chapter{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}