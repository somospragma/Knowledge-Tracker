package com.pragma.vc.tracker.knowledgeapplication.application.dto;

import java.time.LocalDate;

public class CreateAppliedKnowledgeCommand {
    private Long projectId;
    private Long pragmaticId;
    private Long knowledgeId;
    private Long levelId;
    private LocalDate startDate;

    public CreateAppliedKnowledgeCommand() {
    }

    public CreateAppliedKnowledgeCommand(Long projectId, Long pragmaticId, Long knowledgeId, Long levelId, LocalDate startDate) {
        this.projectId = projectId;
        this.pragmaticId = pragmaticId;
        this.knowledgeId = knowledgeId;
        this.levelId = levelId;
        this.startDate = startDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPragmaticId() {
        return pragmaticId;
    }

    public void setPragmaticId(Long pragmaticId) {
        this.pragmaticId = pragmaticId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}