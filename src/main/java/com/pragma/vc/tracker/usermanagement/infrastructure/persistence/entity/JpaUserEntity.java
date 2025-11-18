package com.pragma.vc.tracker.usermanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for User persistence
 * This is in the infrastructure layer and contains framework-specific annotations
 */
@Entity
@Table(name = "\"User\"",
       indexes = {
           @Index(name = "idx_user_email", columnList = "\"email\"", unique = true)
       })
public class JpaUserEntity {

    @Id
    @Column(name = "\"id\"", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "\"email\"", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "\"first_name\"", nullable = false, length = 100)
    private String firstName;

    @Column(name = "\"last_name\"", nullable = false, length = 100)
    private String lastName;

    @Column(name = "\"system_role\"", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SystemRoleEnum systemRole;

    @Column(name = "\"active\"", nullable = false)
    private boolean active;

    @Column(name = "\"created_at\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updated_at\"")
    private LocalDateTime updatedAt;

    // Default constructor for JPA
    public JpaUserEntity() {
    }

    public JpaUserEntity(UUID id, String email, String firstName, String lastName,
                        SystemRoleEnum systemRole, boolean active,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public SystemRoleEnum getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRoleEnum systemRole) {
        this.systemRole = systemRole;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    /**
     * Enum for JPA persistence - mirrors domain SystemRole
     */
    public enum SystemRoleEnum {
        FULL_ADMINISTRATOR,
        KNOWLEDGE_MANAGER,
        PROJECT_ACCOUNT_MANAGER,
        PEOPLE_MANAGER,
        USER
    }
}
