package com.pragma.vc.tracker.peoplemanagement.application.dto;

/**
 * Command for creating a new Chapter
 */
public class CreateChapterCommand {
    private String name;

    public CreateChapterCommand() {
    }

    public CreateChapterCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}