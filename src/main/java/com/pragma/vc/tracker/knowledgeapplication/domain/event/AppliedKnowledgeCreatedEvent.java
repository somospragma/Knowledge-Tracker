package com.pragma.vc.tracker.knowledgeapplication.domain.event;

import com.pragma.vc.tracker.shared.domain.event.DomainEvent;

/**
 * Domain event fired when a Pragmatic applies knowledge to a project.
 * This event is published asynchronously to notify other bounded contexts.
 */
public class AppliedKnowledgeCreatedEvent extends DomainEvent.AbstractDomainEvent {

    private final Long appliedKnowledgeId;
    private final Long projectId;
    private final Long pragmaticId;
    private final Long knowledgeId;
    private final Long levelId;

    public AppliedKnowledgeCreatedEvent(
            Long appliedKnowledgeId,
            Long projectId,
            Long pragmaticId,
            Long knowledgeId,
            Long levelId
    ) {
        super();
        this.appliedKnowledgeId = appliedKnowledgeId;
        this.projectId = projectId;
        this.pragmaticId = pragmaticId;
        this.knowledgeId = knowledgeId;
        this.levelId = levelId;
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

    @Override
    public String toString() {
        return "AppliedKnowledgeCreatedEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", occurredOn=" + getOccurredOn() +
                ", appliedKnowledgeId=" + appliedKnowledgeId +
                ", projectId=" + projectId +
                ", pragmaticId=" + pragmaticId +
                ", knowledgeId=" + knowledgeId +
                ", levelId=" + levelId +
                '}';
    }
}
