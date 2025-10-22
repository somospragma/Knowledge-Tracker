package com.pragma.vc.tracker.projectmanagement.application.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Command for creating a new Project
 */
public class CreateProjectCommand {
    private Long accountId;
    private String name;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String type;
    private Map<String, String> attributes;

    public CreateProjectCommand() {
    }

    public CreateProjectCommand(Long accountId, String name, String status, LocalDateTime startDate,
                               LocalDateTime endDate, String type, Map<String, String> attributes) {
        this.accountId = accountId;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.attributes = attributes;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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