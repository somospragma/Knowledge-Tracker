package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Account Entity - Aggregate Root
 * Represents a client organization in the Project Management bounded context
 *
 * Domain rules:
 * - Account name must be unique and not blank
 * - Territory is optional but if provided must not be blank
 * - Attributes store additional metadata as key-value pairs
 */
public class Account {
    private AccountId id;
    private String name;
    private String territory;
    private AccountStatus status;
    private Map<String, String> attributes;

    // Private constructor to enforce factory method usage
    private Account(AccountId id, String name, String territory, AccountStatus status, Map<String, String> attributes) {
        validateName(name);
        validateTerritory(territory);

        this.id = id;
        this.name = name;
        this.territory = territory;
        this.status = status != null ? status : AccountStatus.ACTIVE;
        this.attributes = new HashMap<>(attributes != null ? attributes : new HashMap<>());
    }

    /**
     * Factory method to create a new Account
     */
    public static Account create(String name, String territory, AccountStatus status, Map<String, String> attributes) {
        return new Account(null, name, territory, status, attributes);
    }

    /**
     * Factory method to reconstitute an Account from persistence
     */
    public static Account reconstitute(AccountId id, String name, String territory, AccountStatus status, Map<String, String> attributes) {
        return new Account(id, name, territory, status, attributes);
    }

    // Business methods

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void updateTerritory(String newTerritory) {
        validateTerritory(newTerritory);
        this.territory = newTerritory;
    }

    public void activate() {
        if (this.status == AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is already active");
        }
        this.status = AccountStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == AccountStatus.INACTIVE) {
            throw new IllegalStateException("Account is already inactive");
        }
        this.status = AccountStatus.INACTIVE;
    }

    public void suspend() {
        if (this.status == AccountStatus.SUSPENDED) {
            throw new IllegalStateException("Account is already suspended");
        }
        this.status = AccountStatus.SUSPENDED;
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

    // Validation methods

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Account name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Account name cannot exceed 255 characters");
        }
    }

    private void validateTerritory(String territory) {
        if (territory != null && territory.isBlank()) {
            throw new IllegalArgumentException("Territory cannot be blank if provided");
        }
        if (territory != null && territory.length() > 255) {
            throw new IllegalArgumentException("Territory cannot exceed 255 characters");
        }
    }

    // Getters

    public AccountId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTerritory() {
        return territory;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes); // Return defensive copy
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    // For infrastructure layer to set ID after persistence
    public void setId(AccountId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Account ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Account{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", territory='" + territory + '\'' +
               ", status=" + status +
               '}';
    }
}