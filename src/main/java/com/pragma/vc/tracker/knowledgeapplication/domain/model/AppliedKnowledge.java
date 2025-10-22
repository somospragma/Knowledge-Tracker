package com.pragma.vc.tracker.knowledgeapplication.domain.model;

import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;

import java.time.LocalDate;
import java.util.Objects;

/**
 * AppliedKnowledge aggregate root representing a Pragmatic applying specific Knowledge
 * with a certain Level on a Project.
 */
public class AppliedKnowledge {
    private AppliedKnowledgeId id;
    private final ProjectId projectId;
    private final PragmaticId pragmaticId;
    private KnowledgeId knowledgeId;
    private LevelId levelId;
    private LocalDate startDate;

    private AppliedKnowledge(
            ProjectId projectId,
            PragmaticId pragmaticId,
            KnowledgeId knowledgeId,
            LevelId levelId,
            LocalDate startDate
    ) {
        validateRequiredFields(projectId, pragmaticId);
        this.projectId = projectId;
        this.pragmaticId = pragmaticId;
        this.knowledgeId = knowledgeId;
        this.levelId = levelId;
        this.startDate = startDate;
    }

    public static AppliedKnowledge create(
            ProjectId projectId,
            PragmaticId pragmaticId,
            KnowledgeId knowledgeId,
            LevelId levelId,
            LocalDate startDate
    ) {
        return new AppliedKnowledge(projectId, pragmaticId, knowledgeId, levelId, startDate);
    }

    public static AppliedKnowledge reconstitute(
            AppliedKnowledgeId id,
            ProjectId projectId,
            PragmaticId pragmaticId,
            KnowledgeId knowledgeId,
            LevelId levelId,
            LocalDate startDate
    ) {
        AppliedKnowledge appliedKnowledge = new AppliedKnowledge(projectId, pragmaticId, knowledgeId, levelId, startDate);
        appliedKnowledge.id = id;
        return appliedKnowledge;
    }

    private void validateRequiredFields(ProjectId projectId, PragmaticId pragmaticId) {
        if (projectId == null) {
            throw new IllegalArgumentException("ProjectId cannot be null");
        }
        if (pragmaticId == null) {
            throw new IllegalArgumentException("PragmaticId cannot be null");
        }
    }

    // Business methods

    /**
     * Updates the knowledge being applied
     */
    public void updateKnowledge(KnowledgeId knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    /**
     * Updates the proficiency level for this applied knowledge
     */
    public void updateLevel(LevelId levelId) {
        if (levelId == null) {
            throw new IllegalArgumentException("LevelId cannot be null");
        }
        this.levelId = levelId;
    }

    /**
     * Updates the start date of when this knowledge was applied
     */
    public void updateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the future");
        }
        this.startDate = startDate;
    }

    /**
     * Checks if this applied knowledge has a knowledge associated
     */
    public boolean hasKnowledge() {
        return this.knowledgeId != null;
    }

    /**
     * Checks if this applied knowledge has a level associated
     */
    public boolean hasLevel() {
        return this.levelId != null;
    }

    /**
     * Business rule: Applied knowledge can only be deleted if it has no associated knowledge or level
     */
    public boolean canBeDeleted() {
        return knowledgeId == null && levelId == null;
    }

    // Getters

    public AppliedKnowledgeId getId() {
        return id;
    }

    public void setId(AppliedKnowledgeId id) {
        if (this.id != null) {
            throw new IllegalStateException("AppliedKnowledge ID cannot be changed once set");
        }
        this.id = id;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public PragmaticId getPragmaticId() {
        return pragmaticId;
    }

    public KnowledgeId getKnowledgeId() {
        return knowledgeId;
    }

    public LevelId getLevelId() {
        return levelId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppliedKnowledge that = (AppliedKnowledge) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AppliedKnowledge{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", pragmaticId=" + pragmaticId +
                ", knowledgeId=" + knowledgeId +
                ", levelId=" + levelId +
                ", startDate=" + startDate +
                '}';
    }
}