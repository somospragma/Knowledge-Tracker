package com.pragma.vc.tracker.knowledgecatalog.domain.model;

/**
 * Value Object representing Knowledge approval status
 */
public enum ApprovalStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    ApprovalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isApproved() {
        return this == APPROVED;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean canBeModified() {
        return this != APPROVED;
    }
}