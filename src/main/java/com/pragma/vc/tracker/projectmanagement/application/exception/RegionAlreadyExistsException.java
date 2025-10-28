package com.pragma.vc.tracker.projectmanagement.application.exception;

/**
 * Exception thrown when trying to create a Region that already exists
 */
public class RegionAlreadyExistsException extends RuntimeException {

    public RegionAlreadyExistsException(String name) {
        super("Region already exists with name: " + name);
    }
}
