package com.pragma.vc.tracker.usermanagement.application.dto;

/**
 * Data Transfer Object for User.
 * Used to transfer user data between layers and to external clients.
 */
public class UserDTO {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String systemRole;
    private boolean active;
    private String keycloakId;

    public UserDTO() {
    }

    public UserDTO(String id, String email, String firstName, String lastName, String systemRole, boolean active) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
        this.active = active;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
