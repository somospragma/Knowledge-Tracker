package com.pragma.vc.tracker.peoplemanagement.domain.model;

import java.util.Objects;

/**
 * Pragmatic Entity - Aggregate Root
 * Represents a Pragma SA employee in the People Management bounded context
 *
 * Domain rules:
 * - Email must be unique across all Pragmatics
 * - First and last names are required
 * - Active Pragmatics must belong to a Chapter
 * - Only active Pragmatics can be assigned to projects
 */
public class Pragmatic {
    private PragmaticId id;
    private ChapterId chapterId;
    private Email email;
    private String firstName;
    private String lastName;
    private PragmaticStatus status;

    // Private constructor to enforce factory method usage
    private Pragmatic(PragmaticId id, ChapterId chapterId, Email email,
                     String firstName, String lastName, PragmaticStatus status) {
        validateNames(firstName, lastName);
        validateChapterForStatus(chapterId, status);

        this.id = id;
        this.chapterId = chapterId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status != null ? status : PragmaticStatus.ACTIVE;
    }

    /**
     * Factory method to create a new Pragmatic
     */
    public static Pragmatic create(ChapterId chapterId, Email email,
                                  String firstName, String lastName, PragmaticStatus status) {
        return new Pragmatic(null, chapterId, email, firstName, lastName, status);
    }

    /**
     * Factory method to reconstitute a Pragmatic from persistence
     */
    public static Pragmatic reconstitute(PragmaticId id, ChapterId chapterId, Email email,
                                        String firstName, String lastName, PragmaticStatus status) {
        return new Pragmatic(id, chapterId, email, firstName, lastName, status);
    }

    // Business methods

    public void updateEmail(Email newEmail) {
        if (newEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        this.email = newEmail;
    }

    public void updateName(String newFirstName, String newLastName) {
        validateNames(newFirstName, newLastName);
        this.firstName = newFirstName;
        this.lastName = newLastName;
    }

    public void updateFirstName(String newFirstName) {
        validateName(newFirstName, "First name");
        this.firstName = newFirstName;
    }

    public void updateLastName(String newLastName) {
        validateName(newLastName, "Last name");
        this.lastName = newLastName;
    }

    public void assignToChapter(ChapterId newChapterId) {
        if (newChapterId == null) {
            throw new IllegalArgumentException("Chapter ID cannot be null");
        }
        this.chapterId = newChapterId;
    }

    public void activate() {
        if (this.status == PragmaticStatus.ACTIVE) {
            throw new IllegalStateException("Pragmatic is already active");
        }
        if (this.chapterId == null) {
            throw new IllegalStateException("Cannot activate Pragmatic without a Chapter assignment");
        }
        this.status = PragmaticStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == PragmaticStatus.INACTIVE) {
            throw new IllegalStateException("Pragmatic is already inactive");
        }
        this.status = PragmaticStatus.INACTIVE;
    }

    public void putOnLeave() {
        if (this.status == PragmaticStatus.ON_LEAVE) {
            throw new IllegalStateException("Pragmatic is already on leave");
        }
        this.status = PragmaticStatus.ON_LEAVE;
    }

    public boolean isActive() {
        return this.status.isActive();
    }

    public boolean canBeAssignedToProjects() {
        return this.status.canBeAssignedToProjects();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Validation methods

    private void validateNames(String firstName, String lastName) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException(fieldName + " cannot exceed 100 characters");
        }
    }

    private void validateChapterForStatus(ChapterId chapterId, PragmaticStatus status) {
        if (status != null && status.isActive() && chapterId == null) {
            throw new IllegalArgumentException("Active Pragmatics must belong to a Chapter");
        }
    }

    // Getters

    public PragmaticId getId() {
        return id;
    }

    public ChapterId getChapterId() {
        return chapterId;
    }

    public Email getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public PragmaticStatus getStatus() {
        return status;
    }

    // For infrastructure layer to set ID after persistence
    public void setId(PragmaticId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Pragmatic ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pragmatic pragmatic = (Pragmatic) o;
        return Objects.equals(id, pragmatic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pragmatic{" +
               "id=" + id +
               ", email=" + email +
               ", name='" + getFullName() + '\'' +
               ", chapterId=" + chapterId +
               ", status=" + status +
               '}';
    }
}