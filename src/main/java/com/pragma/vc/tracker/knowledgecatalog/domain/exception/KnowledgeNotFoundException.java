package com.pragma.vc.tracker.knowledgecatalog.domain.exception;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;

/**
 * Domain exception thrown when Knowledge is not found
 */
public class KnowledgeNotFoundException extends RuntimeException {
    private final KnowledgeId knowledgeId;

    public KnowledgeNotFoundException(KnowledgeId knowledgeId) {
        super("Knowledge not found with id: " + knowledgeId);
        this.knowledgeId = knowledgeId;
    }

    public KnowledgeId getKnowledgeId() {
        return knowledgeId;
    }
}