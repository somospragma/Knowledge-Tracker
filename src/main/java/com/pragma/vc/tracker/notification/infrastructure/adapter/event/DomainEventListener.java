package com.pragma.vc.tracker.notification.infrastructure.adapter.event;

import com.pragma.vc.tracker.knowledgeapplication.domain.event.AppliedKnowledgeCreatedEvent;
import com.pragma.vc.tracker.notification.application.usecase.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Infrastructure adapter that listens to domain events and triggers notifications.
 * Uses Spring's @EventListener to subscribe to events published by other bounded contexts.
 */
@Component
public class DomainEventListener {

    private final NotificationService notificationService;

    public DomainEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Handles AppliedKnowledgeCreatedEvent asynchronously.
     * This listener is triggered when a Pragmatic applies knowledge to a project.
     */
    @Async
    @EventListener
    public void handleAppliedKnowledgeCreated(AppliedKnowledgeCreatedEvent event) {
        notificationService.notifyAppliedKnowledgeCreated(
                event.getEventId(),
                event.getAppliedKnowledgeId(),
                event.getProjectId(),
                event.getPragmaticId(),
                event.getKnowledgeId(),
                event.getLevelId()
        );
    }
}
