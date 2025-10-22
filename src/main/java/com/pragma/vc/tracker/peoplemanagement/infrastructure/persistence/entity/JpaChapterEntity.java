package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * JPA Entity for Chapter persistence
 * This is in the infrastructure layer and contains framework-specific annotations
 */
@Entity
@Table(name = "\"Chapter\"")
public class JpaChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    // Default constructor for JPA
    public JpaChapterEntity() {
    }

    public JpaChapterEntity(Long id, String name) {
        this.id = id;
        this.name = name;
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
}