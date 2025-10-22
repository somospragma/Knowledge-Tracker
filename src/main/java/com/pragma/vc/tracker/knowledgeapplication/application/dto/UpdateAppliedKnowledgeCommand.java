package com.pragma.vc.tracker.knowledgeapplication.application.dto;

import java.time.LocalDate;

public class UpdateAppliedKnowledgeCommand {
    private Long knowledgeId;
    private Long levelId;
    private LocalDate startDate;

    public UpdateAppliedKnowledgeCommand() {
    }

    public UpdateAppliedKnowledgeCommand(Long knowledgeId, Long levelId, LocalDate startDate) {
        this.knowledgeId = knowledgeId;
        this.levelId = levelId;
        this.startDate = startDate;
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