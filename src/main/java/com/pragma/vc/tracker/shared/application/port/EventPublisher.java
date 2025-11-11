package com.pragma.vc.tracker.shared.application.port;

import com.pragma.vc.tracker.shared.domain.event.DomainEvent;

/**
 * Port for publishing domain events asynchronously.
 * This is an output port that will be implemented by infrastructure layer.
 */
public interface EventPublisher {

    /**
     * Publishes a domain event asynchronously
     * @param event the domain event to publish
     */
    void publish(DomainEvent event);
}
