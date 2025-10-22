package com.pragma.vc.tracker.projectmanagement.domain.model;

/**
 * Value Object representing Account status
 */
public enum AccountStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended");

    private final String displayName;

    AccountStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }
}