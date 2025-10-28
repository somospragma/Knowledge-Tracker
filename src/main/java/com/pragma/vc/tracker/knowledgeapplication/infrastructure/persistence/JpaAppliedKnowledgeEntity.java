package com.pragma.vc.tracker.knowledgeapplication.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"Applied_Knowledge\"")
public class JpaAppliedKnowledgeEntity {

    @Id
    @Column(name = "\"id\"")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"project_id\"", nullable = false)
    private Long projectId;

    @Column(name = "\"pragmatic_id\"", nullable = false)
    private Long pragmaticId;

    @Column(name = "\"knowledge_id\"", nullable = false)
    private Long knowledgeId;

    @Column(name = "\"onboard_date\"", nullable = false)
    private LocalDate onboardDate;

    @Column(name = "\"offboard_date\"")
    private LocalDate offboardDate;

    @Column(name = "\"knowledge_level\"", nullable = false)
    private Long knowledgeLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "\"attributes\"", columnDefinition = "jsonb")
    private String attributes;

    @Column(name = "\"created_at\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updated_at\"")
    private LocalDateTime updatedAt;

    public JpaAppliedKnowledgeEntity() {
    }

    public JpaAppliedKnowledgeEntity(Long projectId, Long pragmaticId, Long knowledgeId,
                                    LocalDate onboardDate, LocalDate offboardDate, Long knowledgeLevel,
                                    String attributes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.projectId = projectId;
        this.pragmaticId = pragmaticId;
        this.knowledgeId = knowledgeId;
        this.onboardDate = onboardDate;
        this.offboardDate = offboardDate;
        this.knowledgeLevel = knowledgeLevel;
        this.attributes = attributes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPragmaticId() {
        return pragmaticId;
    }

    public void setPragmaticId(Long pragmaticId) {
        this.pragmaticId = pragmaticId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public LocalDate getOnboardDate() {
        return onboardDate;
    }

    public void setOnboardDate(LocalDate onboardDate) {
        this.onboardDate = onboardDate;
    }

    public LocalDate getOffboardDate() {
        return offboardDate;
    }

    public void setOffboardDate(LocalDate offboardDate) {
        this.offboardDate = offboardDate;
    }

    public Long getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(Long knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}