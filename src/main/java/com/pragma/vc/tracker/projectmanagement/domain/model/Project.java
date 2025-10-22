package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Project Entity - Aggregate Root
 * Represents a client project in the Project Management bounded context
 *
 * Domain rules:
 * - Project must belong to an Account (accountId required)
 * - Project name must not be blank
 * - Start date is required for active projects
 * - End date must be after start date if provided
 * - Project type defaults to ABIERTO if not specified
 */
public class Project {
    private ProjectId id;
    private AccountId accountId;
    private String name;
    private ProjectStatus status;
    private DateRange dateRange;
    private ProjectType type;
    private Map<String, String> attributes;

    // Private constructor to enforce factory method usage
    private Project(ProjectId id, AccountId accountId, String name, ProjectStatus status,
                   DateRange dateRange, ProjectType type, Map<String, String> attributes) {
        validateAccountId(accountId);
        validateName(name);
        validateDateRangeForStatus(dateRange, status);

        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.status = status != null ? status : ProjectStatus.ACTIVE;
        this.dateRange = dateRange;
        this.type = type != null ? type : ProjectType.ABIERTO;
        this.attributes = new HashMap<>(attributes != null ? attributes : new HashMap<>());
    }

    /**
     * Factory method to create a new Project
     */
    public static Project create(AccountId accountId, String name, ProjectStatus status,
                                DateRange dateRange, ProjectType type, Map<String, String> attributes) {
        return new Project(null, accountId, name, status, dateRange, type, attributes);
    }

    /**
     * Factory method to reconstitute a Project from persistence
     */
    public static Project reconstitute(ProjectId id, AccountId accountId, String name, ProjectStatus status,
                                      DateRange dateRange, ProjectType type, Map<String, String> attributes) {
        return new Project(id, accountId, name, status, dateRange, type, attributes);
    }

    // Business methods

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void updateDateRange(DateRange newDateRange) {
        validateDateRangeForStatus(newDateRange, this.status);
        this.dateRange = newDateRange;
    }

    public void updateType(ProjectType newType) {
        if (newType == null) {
            throw new IllegalArgumentException("Project type cannot be null");
        }
        this.type = newType;
    }

    public void activate() {
        if (this.status == ProjectStatus.ACTIVE) {
            throw new IllegalStateException("Project is already active");
        }
        if (this.dateRange == null) {
            throw new IllegalStateException("Cannot activate project without a start date");
        }
        this.status = ProjectStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == ProjectStatus.INACTIVE) {
            throw new IllegalStateException("Project is already inactive");
        }
        this.status = ProjectStatus.INACTIVE;
    }

    public void complete() {
        if (this.status == ProjectStatus.COMPLETED) {
            throw new IllegalStateException("Project is already completed");
        }
        this.status = ProjectStatus.COMPLETED;
    }

    public void putOnHold() {
        if (this.status == ProjectStatus.ON_HOLD) {
            throw new IllegalStateException("Project is already on hold");
        }
        if (this.status == ProjectStatus.COMPLETED) {
            throw new IllegalStateException("Cannot put a completed project on hold");
        }
        this.status = ProjectStatus.ON_HOLD;
    }

    public void addAttribute(String key, String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Attribute key cannot be null or blank");
        }
        this.attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    public boolean isActive() {
        return this.status.isActive();
    }

    public boolean canAcceptNewAssignments() {
        return this.status.canAcceptNewAssignments();
    }

    public boolean isOngoing() {
        return this.dateRange != null && this.dateRange.isOngoing();
    }

    // Validation methods

    private void validateAccountId(AccountId accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Project must belong to an Account");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Project name cannot exceed 255 characters");
        }
    }

    private void validateDateRangeForStatus(DateRange dateRange, ProjectStatus status) {
        if (status != null && status.isActive() && dateRange == null) {
            throw new IllegalArgumentException("Active projects must have a start date");
        }
    }

    // Getters

    public ProjectId getId() {
        return id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public ProjectType getType() {
        return type;
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes); // Return defensive copy
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    // For infrastructure layer to set ID after persistence
    public void setId(ProjectId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Project ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Project{" +
               "id=" + id +
               ", accountId=" + accountId +
               ", name='" + name + '\'' +
               ", status=" + status +
               ", type=" + type +
               ", dateRange=" + dateRange +
               '}';
    }
}