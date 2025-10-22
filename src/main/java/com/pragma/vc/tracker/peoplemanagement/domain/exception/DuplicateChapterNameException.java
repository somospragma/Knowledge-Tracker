package com.pragma.vc.tracker.peoplemanagement.domain.exception;

/**
 * Domain exception thrown when attempting to create a Chapter with a duplicate name
 */
public class DuplicateChapterNameException extends RuntimeException {
    private final String chapterName;

    public DuplicateChapterNameException(String chapterName) {
        super("Chapter already exists with name: " + chapterName);
        this.chapterName = chapterName;
    }

    public String getChapterName() {
        return chapterName;
    }
}