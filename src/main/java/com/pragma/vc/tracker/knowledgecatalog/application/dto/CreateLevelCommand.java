package com.pragma.vc.tracker.knowledgecatalog.application.dto;

import java.util.Map;

public class CreateLevelCommand {
    private String name;
    private Map<String, String> attributes;

    public CreateLevelCommand() {
    }

    public CreateLevelCommand(String name, Map<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
