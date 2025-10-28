package com.pragma.vc.tracker.projectmanagement.application.dto;

/**
 * Command for creating a new Region
 */
public class CreateRegionCommand {
    private String name;

    public CreateRegionCommand() {
    }

    public CreateRegionCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
