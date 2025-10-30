package com.pragma.vc.tracker.projectmanagement.application.exception;

/**
 * Exception thrown when trying to create a Territory that already exists
 */
public class TerritoryAlreadyExistsException extends RuntimeException {

    public TerritoryAlreadyExistsException(String name) {
        super("Territory already exists with name: " + name);
    }
}
