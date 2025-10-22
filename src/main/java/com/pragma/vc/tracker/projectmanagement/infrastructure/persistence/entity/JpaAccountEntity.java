package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * JPA Entity for Account persistence
 * This is in the infrastructure layer and contains framework-specific annotations
 */
@Entity
@Table(name = "\"Account\"")
public class JpaAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"name\"", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "region", length = 255)
    private String region;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "attributes", length = 5000)
    private String attributes;

    // Default constructor for JPA
    public JpaAccountEntity() {
    }

    public JpaAccountEntity(Long id, String name, String region, String status, String attributes) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.status = status;
        this.attributes = attributes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}