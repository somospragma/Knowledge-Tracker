package com.pragma.vc.tracker.shared.infrastructure.adapter.event;

import com.pragma.vc.tracker.shared.application.port.EventPublisher;
import com.pragma.vc.tracker.shared.domain.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Spring-based implementation of EventPublisher.
 * Uses Spring's ApplicationEventPublisher for async event publishing.
 */
@Component
public class SpringEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Async
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
