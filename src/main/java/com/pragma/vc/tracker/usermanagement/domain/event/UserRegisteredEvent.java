package com.pragma.vc.tracker.usermanagement.domain.event;

import com.pragma.vc.tracker.shared.domain.event.DomainEvent;
import com.pragma.vc.tracker.usermanagement.domain.model.SystemRole;

/**
 * Domain event fired when a new user is registered in the system.
 * This event is published asynchronously to notify other bounded contexts
 * and external systems (e.g., Keycloak) about the new user.
 */
public class UserRegisteredEvent extends DomainEvent.AbstractDomainEvent {

    private final String userId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final SystemRole systemRole;

    public UserRegisteredEvent(
            String userId,
            String email,
            String firstName,
            String lastName,
            SystemRole systemRole
    ) {
        super();
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SystemRole getSystemRole() {
        return systemRole;
    }

    @Override
    public String toString() {
        return "UserRegisteredEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", occurredOn=" + getOccurredOn() +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", systemRole=" + systemRole +
                '}';
    }
}
