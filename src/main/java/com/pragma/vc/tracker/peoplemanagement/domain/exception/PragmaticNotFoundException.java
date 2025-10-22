package com.pragma.vc.tracker.peoplemanagement.domain.exception;

import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;

/**
 * Domain exception thrown when a Pragmatic is not found
 */
public class PragmaticNotFoundException extends RuntimeException {
    private final PragmaticId pragmaticId;

    public PragmaticNotFoundException(PragmaticId pragmaticId) {
        super("Pragmatic not found with id: " + pragmaticId);
        this.pragmaticId = pragmaticId;
    }

    public PragmaticId getPragmaticId() {
        return pragmaticId;
    }
}