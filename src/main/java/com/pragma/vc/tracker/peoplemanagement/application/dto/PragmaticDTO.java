package com.pragma.vc.tracker.peoplemanagement.application.dto;

/**
 * Data Transfer Object for Pragmatic
 */
public class PragmaticDTO {
    private Long id;
    private Long chapterId;
    private String chapterName;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String status;

    public PragmaticDTO() {
    }

    public PragmaticDTO(Long id, Long chapterId, String chapterName, String email,
                       String firstName, String lastName, String fullName, String status) {
        this.id = id;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
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

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}