package com.pragma.vc.tracker.peoplemanagement.application.dto;

/**
 * Command for creating a new Pragmatic
 */
public class CreatePragmaticCommand {
    private Long chapterId;
    private String email;
    private String firstName;
    private String lastName;
    private String status;

    public CreatePragmaticCommand() {
    }

    public CreatePragmaticCommand(Long chapterId, String email, String firstName,
                                 String lastName, String status) {
        this.chapterId = chapterId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
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