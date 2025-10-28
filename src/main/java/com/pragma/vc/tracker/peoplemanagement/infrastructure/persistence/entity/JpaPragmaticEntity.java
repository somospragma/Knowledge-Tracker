package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

/**
 * JPA Entity for Pragmatic persistence
 * This is in the infrastructure layer and contains framework-specific annotations
 */
@Entity
@Table(name = "\"Pragmatic\"")
public class JpaPragmaticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"chapter_id\"", nullable = false)
    private Long chapterId;

    @Column(name = "\"email\"", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "\"first_name\"", length = 100)
    private String firstName;

    @Column(name = "\"last_name\"", length = 100)
    private String lastName;

    @Column(name = "\"status\"", length = 50)
    private String status;

    @Column(name = "\"created_at\"", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updated_at\"", nullable = false)
    private LocalDateTime updatedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "\"attributes\"", columnDefinition = "jsonb")
    private String attributes;

    // Default constructor for JPA
    public JpaPragmaticEntity() {
    }

    public JpaPragmaticEntity(Long id, Long chapterId, String email,
                             String firstName, String lastName, String status,
                             LocalDateTime createdAt, LocalDateTime updatedAt, String attributes) {
        this.id = id;
        this.chapterId = chapterId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributes = attributes;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
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

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}