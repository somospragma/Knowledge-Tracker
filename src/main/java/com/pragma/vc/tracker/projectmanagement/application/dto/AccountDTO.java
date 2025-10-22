package com.pragma.vc.tracker.projectmanagement.application.dto;

import java.util.Map;

/**
 * Data Transfer Object for Account
 */
public class AccountDTO {
    private Long id;
    private String name;
    private String region;
    private String status;
    private Map<String, String> attributes;

    public AccountDTO() {
    }

    public AccountDTO(Long id, String name, String region, String status, Map<String, String> attributes) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.status = status;
        this.attributes = attributes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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