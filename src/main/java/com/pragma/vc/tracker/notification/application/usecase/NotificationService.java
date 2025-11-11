package com.pragma.vc.tracker.notification.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application service for the Notification bounded context.
 * Currently handles logging of domain events from other bounded contexts.
 * This is a generic subdomain that can be extended for email, SMS, push notifications, etc.
 */
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Logs a notification message
     * @param eventType The type of event that triggered this notification
     * @param message The message to log
     */
    public void logNotification(String eventType, String message) {
        logger.info("[NOTIFICATION] EventType={}, Message={}", eventType, message);
    }

    /**
     * Logs an applied knowledge creation event
     */
    public void notifyAppliedKnowledgeCreated(
            String eventId,
            Long appliedKnowledgeId,
            Long projectId,
            Long pragmaticId,
            Long knowledgeId,
            Long levelId
    ) {
        String message = String.format(
                "Applied Knowledge created: ID=%d, ProjectID=%d, PragmaticID=%d, KnowledgeID=%s, LevelID=%s",
                appliedKnowledgeId,
                projectId,
                pragmaticId,
                knowledgeId != null ? knowledgeId.toString() : "null",
                levelId != null ? levelId.toString() : "null"
        );
        logger.info("[NOTIFICATION] EventID={}, {}", eventId, message);
    }
}
