package com.pragma.vc.tracker.projectmanagement.application.dto;

/**
 * Command for creating a new Territory
 */
public class CreateTerritoryCommand {
    private String name;

    public CreateTerritoryCommand() {
    }

    public CreateTerritoryCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
