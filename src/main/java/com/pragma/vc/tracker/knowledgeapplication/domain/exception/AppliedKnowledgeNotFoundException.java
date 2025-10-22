package com.pragma.vc.tracker.knowledgeapplication.domain.exception;

public class AppliedKnowledgeNotFoundException extends RuntimeException {
    public AppliedKnowledgeNotFoundException(Long id) {
        super("Applied knowledge not found with id: " + id);
    }
}