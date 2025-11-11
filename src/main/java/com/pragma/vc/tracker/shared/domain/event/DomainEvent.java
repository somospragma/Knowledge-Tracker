package com.pragma.vc.tracker.shared.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Base interface for all domain events in the system.
 * Domain events represent something that happened in the domain that domain experts care about.
 */
public interface DomainEvent {

    /**
     * Unique identifier for this event instance
     */
    String getEventId();

    /**
     * When the event occurred
     */
    Instant getOccurredOn();

    /**
     * The type of event (used for routing and logging)
     */
    String getEventType();

    /**
     * Base abstract implementation providing common event infrastructure
     */
    abstract class AbstractDomainEvent implements DomainEvent {
        private final String eventId;
        private final Instant occurredOn;

        protected AbstractDomainEvent() {
            this.eventId = UUID.randomUUID().toString();
            this.occurredOn = Instant.now();
        }

        @Override
        public String getEventId() {
            return eventId;
        }

        @Override
        public Instant getOccurredOn() {
            return occurredOn;
        }

        @Override
        public String getEventType() {
            return this.getClass().getSimpleName();
        }
    }
}