package com.pragma.vc.tracker.projectmanagement.domain.model;

/**
 * Value Object representing Project status
 */
public enum ProjectStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    COMPLETED("Completed"),
    ON_HOLD("On Hold");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canAcceptNewAssignments() {
        return this == ACTIVE || this == ON_HOLD;
    }
}