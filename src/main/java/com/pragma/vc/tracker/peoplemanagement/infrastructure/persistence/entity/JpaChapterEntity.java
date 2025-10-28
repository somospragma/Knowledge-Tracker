package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity for Chapter persistence
 * This is in the infrastructure layer and contains framework-specific annotations
 */
@Entity
@Table(name = "\"Chapter\"")
public class JpaChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"kc_id\"", nullable = false)
    private Long kcId;

    @Column(name = "\"name\"", nullable = false, length = 255)
    private String name;

    @Column(name = "\"status\"", nullable = false, length = 50)
    private String status;

    @Column(name = "\"created_at\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updated_at\"")
    private LocalDateTime updatedAt;

    // Default constructor for JPA
    public JpaChapterEntity() {
    }

    public JpaChapterEntity(Long id, Long kcId, String name, String status,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.kcId = kcId;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "Active";
        }
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

    public Long getKcId() {
        return kcId;
    }

    public void setKcId(Long kcId) {
        this.kcId = kcId;
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