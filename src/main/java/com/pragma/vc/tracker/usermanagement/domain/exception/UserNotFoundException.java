package com.pragma.vc.tracker.usermanagement.domain.exception;

import com.pragma.vc.tracker.usermanagement.domain.model.UserId;

/**
 * Domain exception thrown when a user cannot be found
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UserId userId) {
        super("User not found with ID: " + userId.getValue());
    }

    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
