package com.pragma.vc.tracker.usermanagement.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * User Entity - Aggregate Root
 * Represents a system user in the User Management bounded context
 *
 * Domain rules:
 * - Email must be unique and valid format
 * - First name and last name are required
 * - Every user must have a system role
 * - Email is used as the primary identifier for login
 */
public class User {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private UserId id;
    private String email;
    private String firstName;
    private String lastName;
    private SystemRole systemRole;
    private boolean active;

    // Private constructor to enforce factory method usage
    private User(UserId id, String email, String firstName, String lastName,
                SystemRole systemRole, boolean active) {
        validateEmail(email);
        validateFirstName(firstName);
        validateLastName(lastName);
        validateSystemRole(systemRole);

        this.id = id;
        this.email = email.toLowerCase().trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.systemRole = systemRole;
        this.active = active;
    }

    /**
     * Factory method to create a new User
     */
    public static User create(String email, String firstName, String lastName, SystemRole systemRole) {
        return new User(UserId.generate(), email, firstName, lastName, systemRole, true);
    }

    /**
     * Factory method to reconstitute a User from persistence
     */
    public static User reconstitute(UserId id, String email, String firstName, String lastName,
                                   SystemRole systemRole, boolean active) {
        return new User(id, email, firstName, lastName, systemRole, active);
    }

    // Business methods

    public void updateEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = newEmail.toLowerCase().trim();
    }

    public void updateFirstName(String newFirstName) {
        validateFirstName(newFirstName);
        this.firstName = newFirstName.trim();
    }

    public void updateLastName(String newLastName) {
        validateLastName(newLastName);
        this.lastName = newLastName.trim();
    }

    public void updateName(String newFirstName, String newLastName) {
        validateFirstName(newFirstName);
        validateLastName(newLastName);
        this.firstName = newFirstName.trim();
        this.lastName = newLastName.trim();
    }

    public void assignRole(SystemRole newRole) {
        validateSystemRole(newRole);
        this.systemRole = newRole;
    }

    public void activate() {
        if (this.active) {
            throw new IllegalStateException("User is already active");
        }
        this.active = true;
    }

    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("User is already inactive");
        }
        this.active = false;
    }

    public boolean hasPermission(SystemRole.Permission permission) {
        return this.systemRole.hasPermission(permission);
    }

    public boolean isAdministrator() {
        return this.systemRole.isAdministrator();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Validation methods

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        String trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        if (trimmedEmail.length() > 255) {
            throw new IllegalArgumentException("Email cannot exceed 255 characters");
        }
    }

    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (firstName.trim().length() > 100) {
            throw new IllegalArgumentException("First name cannot exceed 100 characters");
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if (lastName.trim().length() > 100) {
            throw new IllegalArgumentException("Last name cannot exceed 100 characters");
        }
    }

    private void validateSystemRole(SystemRole systemRole) {
        if (systemRole == null) {
            throw new IllegalArgumentException("System role cannot be null");
        }
    }

    // Getters

    public UserId getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SystemRole getSystemRole() {
        return systemRole;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", systemRole=" + systemRole +
               ", active=" + active +
               '}';
    }
}
