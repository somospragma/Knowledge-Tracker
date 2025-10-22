package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.Objects;

/**
 * Value Object representing Project identifier
 * Immutable and self-validating
 */
public class ProjectId {
    private final Long value;

    private ProjectId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Project ID must be a positive number");
        }
        this.value = value;
    }

    public static ProjectId of(Long value) {
        return new ProjectId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectId projectId = (ProjectId) o;
        return Objects.equals(value, projectId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ProjectId{" + value + "}";
    }
}