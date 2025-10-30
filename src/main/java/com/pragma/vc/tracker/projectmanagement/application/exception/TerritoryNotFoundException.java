package com.pragma.vc.tracker.projectmanagement.application.exception;

import com.pragma.vc.tracker.projectmanagement.domain.model.TerritoryId;

/**
 * Exception thrown when a Territory is not found
 */
public class TerritoryNotFoundException extends RuntimeException {

    public TerritoryNotFoundException(TerritoryId territoryId) {
        super("Territory not found with ID: " + territoryId.getValue());
    }

    public TerritoryNotFoundException(String name) {
        super("Territory not found with name: " + name);
    }
}
