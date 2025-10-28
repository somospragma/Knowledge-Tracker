package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity for Project persistence
 * This is in the infrastructure layer and contains framework-specific annotations
 */
@Entity
@Table(name = "\"Project\"")
public class JpaProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"account_id\"", nullable = false)
    private Long accountId;

    @Column(name = "\"name\"", nullable = false, length = 255)
    private String name;

    @Column(name = "\"status\"", nullable = false, length = 255)
    private String status;

    @Column(name = "\"start_date\"", nullable = false)
    private LocalDate startDate;

    @Column(name = "\"end_date\"")
    private LocalDate endDate;

    @Column(name = "\"type\"", length = 255)
    private String type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "\"attributes\"", columnDefinition = "jsonb")
    private String attributes;

    @Column(name = "\"created_at\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updated_at\"")
    private LocalDateTime updatedAt;

    // Default constructor for JPA
    public JpaProjectEntity() {
    }

    public JpaProjectEntity(Long id, Long accountId, String name, String status,
                           LocalDate startDate, LocalDate endDate, String type, String attributes,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
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