package com.pragma.vc.tracker.peoplemanagement.domain.exception;

import com.pragma.vc.tracker.peoplemanagement.domain.model.ChapterId;

/**
 * Domain exception thrown when a Chapter is not found
 */
public class ChapterNotFoundException extends RuntimeException {
    private final ChapterId chapterId;

    public ChapterNotFoundException(ChapterId chapterId) {
        super("Chapter not found with id: " + chapterId);
        this.chapterId = chapterId;
    }

    public ChapterId getChapterId() {
        return chapterId;
    }
}