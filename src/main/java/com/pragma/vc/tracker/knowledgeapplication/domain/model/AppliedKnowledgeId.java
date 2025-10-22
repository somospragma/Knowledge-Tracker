package com.pragma.vc.tracker.knowledgeapplication.domain.model;

import java.util.Objects;

public class AppliedKnowledgeId {
    private final Long value;

    private AppliedKnowledgeId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("AppliedKnowledgeId must be a positive number");
        }
        this.value = value;
    }

    public static AppliedKnowledgeId of(Long value) {
        return new AppliedKnowledgeId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppliedKnowledgeId that = (AppliedKnowledgeId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "AppliedKnowledgeId{" + value + '}';
    }
}
