package com.pragma.vc.tracker.knowledgecatalog.application.dto;

import java.util.Map;

public class CreateKnowledgeCommand {
    private Long categoryId;
    private String name;
    private String description;
    private String approvalStatus;
    private Map<String, String> attributes;

    public CreateKnowledgeCommand() {
    }

    public CreateKnowledgeCommand(Long categoryId, String name, String description,
                                 String approvalStatus, Map<String, String> attributes) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.approvalStatus = approvalStatus;
        this.attributes = attributes;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}