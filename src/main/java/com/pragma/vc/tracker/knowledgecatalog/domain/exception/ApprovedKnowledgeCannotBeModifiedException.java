package com.pragma.vc.tracker.knowledgecatalog.domain.exception;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;

/**
 * Domain exception thrown when attempting to modify approved knowledge
 */
public class ApprovedKnowledgeCannotBeModifiedException extends RuntimeException {
    private final KnowledgeId knowledgeId;

    public ApprovedKnowledgeCannotBeModifiedException(KnowledgeId knowledgeId) {
        super("Approved knowledge cannot be modified: " + knowledgeId);
        this.knowledgeId = knowledgeId;
    }

    public KnowledgeId getKnowledgeId() {
        return knowledgeId;
    }
}