package com.pragma.vc.tracker.projectmanagement.application.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object for Project
 */
public class ProjectDTO {
    private Long id;
    private Long accountId;
    private String accountName;
    private String name;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String type;
    private Map<String, String> attributes;

    public ProjectDTO() {
    }

    public ProjectDTO(Long id, Long accountId, String accountName, String name, String status,
                     LocalDateTime startDate, LocalDateTime endDate, String type, Map<String, String> attributes) {
        this.id = id;
        this.accountId = accountId;
        this.accountName = accountName;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.attributes = attributes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}