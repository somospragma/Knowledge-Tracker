package com.pragma.vc.tracker.peoplemanagement.domain.exception;

import com.pragma.vc.tracker.peoplemanagement.domain.model.Email;

/**
 * Domain exception thrown when attempting to create a Pragmatic with a duplicate email
 */
public class DuplicateEmailException extends RuntimeException {
    private final Email email;

    public DuplicateEmailException(Email email) {
        super("Pragmatic already exists with email: " + email);
        this.email = email;
    }

    public Email getEmail() {
        return email;
    }
}