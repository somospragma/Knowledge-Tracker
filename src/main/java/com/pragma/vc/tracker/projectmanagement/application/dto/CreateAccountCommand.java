package com.pragma.vc.tracker.projectmanagement.application.dto;

import java.util.Map;

/**
 * Command for creating a new Account
 */
public class CreateAccountCommand {
    private String name;
    private String region;
    private String status;
    private Map<String, String> attributes;

    public CreateAccountCommand() {
    }

    public CreateAccountCommand(String name, String region, String status, Map<String, String> attributes) {
        this.name = name;
        this.region = region;
        this.status = status;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}