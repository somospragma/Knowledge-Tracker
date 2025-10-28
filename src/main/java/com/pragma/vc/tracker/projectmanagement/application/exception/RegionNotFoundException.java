package com.pragma.vc.tracker.projectmanagement.application.exception;

import com.pragma.vc.tracker.projectmanagement.domain.model.RegionId;

/**
 * Exception thrown when a Region is not found
 */
public class RegionNotFoundException extends RuntimeException {

    public RegionNotFoundException(RegionId regionId) {
        super("Region not found with ID: " + regionId.getValue());
    }

    public RegionNotFoundException(String name) {
        super("Region not found with name: " + name);
    }
}
