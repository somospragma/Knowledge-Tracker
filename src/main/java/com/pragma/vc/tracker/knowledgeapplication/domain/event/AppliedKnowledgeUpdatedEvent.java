package com.pragma.vc.tracker.knowledgeapplication.domain.event;

import com.pragma.vc.tracker.shared.domain.event.DomainEvent;

import java.time.LocalDate;

/**
 * Domain event fired when Applied Knowledge is updated.
 * This event is published asynchronously to notify other bounded contexts.
 */
public class AppliedKnowledgeUpdatedEvent extends DomainEvent.AbstractDomainEvent {

    private final Long appliedKnowledgeId;
    private final Long projectId;
    private final Long pragmaticId;
    private final Long knowledgeId;
    private final Long levelId;
    private final LocalDate startDate;

    public AppliedKnowledgeUpdatedEvent(
            Long appliedKnowledgeId,
            Long projectId,
            Long pragmaticId,
            Long knowledgeId,
            Long levelId,
            LocalDate startDate
    ) {
        super();
        this.appliedKnowledgeId = appliedKnowledgeId;
        this.projectId = projectId;
        this.pragmaticId = pragmaticId;
        this.knowledgeId = knowledgeId;
        this.levelId = levelId;
        this.startDate = startDate;
    }

    public Long getAppliedKnowledgeId() {
        return appliedKnowledgeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getPragmaticId() {
        return pragmaticId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "AppliedKnowledgeUpdatedEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", occurredOn=" + getOccurredOn() +
                ", appliedKnowledgeId=" + appliedKnowledgeId +
                ", projectId=" + projectId +
                ", pragmaticId=" + pragmaticId +
                ", knowledgeId=" + knowledgeId +
                ", levelId=" + levelId +
                ", startDate=" + startDate +
                '}';
    }
}
