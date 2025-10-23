package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "\"knowledge\"")
public class JpaKnowledgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"category_id\"", nullable = false)
    private Long categoryId;

    @Column(name = "\"name\"", nullable = false, length = 255)
    private String name;

    @Column(name = "\"description\"", length = 500)
    private String description;

    @Column(name = "\"approved_status\"", length = 255)
    private String approvalStatus;

    @Column(name = "\"attributes\"", length = 5000)
    private String attributes;

    public JpaKnowledgeEntity() {
    }

    public JpaKnowledgeEntity(Long id, Long categoryId, String name, String description,
                             String approvalStatus, String attributes) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.approvalStatus = approvalStatus;
        this.attributes = attributes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}