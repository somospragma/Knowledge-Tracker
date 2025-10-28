package com.pragma.vc.tracker.projectmanagement.application.dto;

import java.time.LocalDate;
import java.util.Map;

/**
 * Command for creating a new Project
 */
public class CreateProjectCommand {
    private Long accountId;
    private String name;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private Map<String, String> attributes;

    public CreateProjectCommand() {
    }

    public CreateProjectCommand(Long accountId, String name, String status, LocalDate startDate,
                               LocalDate endDate, String type, Map<String, String> attributes) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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