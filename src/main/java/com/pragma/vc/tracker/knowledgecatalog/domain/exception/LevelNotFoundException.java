package com.pragma.vc.tracker.knowledgecatalog.domain.exception;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;

/**
 * Domain exception thrown when a Level is not found
 */
public class LevelNotFoundException extends RuntimeException {
    private final LevelId levelId;

    public LevelNotFoundException(LevelId levelId) {
        super("Level not found with id: " + levelId);
        this.levelId = levelId;
    }

    public LevelId getLevelId() {
        return levelId;
    }
}