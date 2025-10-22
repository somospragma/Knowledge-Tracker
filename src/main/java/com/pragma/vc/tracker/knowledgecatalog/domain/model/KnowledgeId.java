package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import java.util.Objects;

/**
 * Value Object representing Knowledge identifier
 * Immutable and self-validating
 */
public class KnowledgeId {
    private final Long value;

    private KnowledgeId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Knowledge ID must be a positive number");
        }
        this.value = value;
    }

    public static KnowledgeId of(Long value) {
        return new KnowledgeId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeId that = (KnowledgeId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "KnowledgeId{" + value + "}";
    }
}
