package com.pragma.vc.tracker.peoplemanagement.domain.model;

/**
 * Value Object representing Pragmatic employment status
 */
public enum PragmaticStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_LEAVE("On Leave");

    private final String displayName;

    PragmaticStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canBeAssignedToProjects() {
        return this == ACTIVE;
    }
}