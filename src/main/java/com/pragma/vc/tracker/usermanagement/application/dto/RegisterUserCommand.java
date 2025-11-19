package com.pragma.vc.tracker.usermanagement.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Command for registering a new user in the system.
 * This command will create the user both in the local database and in Keycloak.
 */
public class RegisterUserCommand {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotNull(message = "System role is required")
    private String systemRole;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private boolean temporaryPassword = false;

    public RegisterUserCommand() {
    }

    public RegisterUserCommand(String email, String firstName, String lastName, String systemRole, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTemporaryPassword() {
        return temporaryPassword;
    }

    public void setTemporaryPassword(boolean temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }
}
