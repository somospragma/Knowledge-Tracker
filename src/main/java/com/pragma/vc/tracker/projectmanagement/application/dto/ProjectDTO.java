package com.pragma.vc.tracker.projectmanagement.application.dto;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private Map<String, String> attributes;

    public ProjectDTO() {
    }

    public ProjectDTO(Long id, Long accountId, String accountName, String name, String status,
                     LocalDate startDate, LocalDate endDate, String type, Map<String, String> attributes) {
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