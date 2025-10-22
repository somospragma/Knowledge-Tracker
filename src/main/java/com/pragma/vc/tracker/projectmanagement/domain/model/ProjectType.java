package com.pragma.vc.tracker.projectmanagement.domain.model;

/**
 * Value Object representing Project type
 */
public enum ProjectType {
    ABIERTO("Abierto"),      // Open - Time & Materials
    CERRADO("Cerrado"),      // Closed - Fixed Price
    N_A("N/A");              // Not Applicable

    private final String displayName;

    ProjectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isOpenEnded() {
        return this == ABIERTO;
    }

    public boolean isFixedScope() {
        return this == CERRADO;
    }
}