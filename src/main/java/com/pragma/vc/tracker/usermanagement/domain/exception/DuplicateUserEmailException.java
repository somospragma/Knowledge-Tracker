package com.pragma.vc.tracker.usermanagement.domain.exception;

/**
 * Domain exception thrown when attempting to create a user with an email that already exists
 */
public class DuplicateUserEmailException extends RuntimeException {

    public DuplicateUserEmailException(String email) {
        super("User with email already exists: " + email);
    }

    public DuplicateUserEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
