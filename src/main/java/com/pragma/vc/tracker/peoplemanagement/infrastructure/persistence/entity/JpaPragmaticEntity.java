package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;

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

    @Column(name = "\"chapter_id\"")
    private Long chapterId;

    @Column(name = "\"email\"", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "\"first_name\"", length = 100)
    private String firstName;

    @Column(name = "\"last_name\"", length = 100)
    private String lastName;

    @Column(name = "\"status\"", length = 50)
    private String status;

    // Default constructor for JPA
    public JpaPragmaticEntity() {
    }

    public JpaPragmaticEntity(Long id, Long chapterId, String email,
                             String firstName, String lastName, String status) {
        this.id = id;
        this.chapterId = chapterId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
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
}