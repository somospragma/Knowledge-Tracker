package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
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

    @Column(name = "\"startDate\"")
    private LocalDateTime startDate;

    @Column(name = "\"endDate\"")
    private LocalDateTime endDate;

    @Column(name = "\"type\"", length = 255)
    private String type;

    @Column(name = "\"attributes\"", length = 5000)
    private String attributes;

    // Default constructor for JPA
    public JpaProjectEntity() {
    }

    public JpaProjectEntity(Long id, Long accountId, String name, String status,
                           LocalDateTime startDate, LocalDateTime endDate, String type, String attributes) {
        this.id = id;
        this.accountId = accountId;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}