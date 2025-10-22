package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Knowledge Entity - Aggregate Root
 * Represents a technical knowledge item (platform, language, framework, tool, technique)
 *
 * Domain rules:
 * - Knowledge must belong to exactly one category
 * - Knowledge name must not be blank
 * - Approved knowledge cannot be deleted or modified
 * - Rejected knowledge can be resubmitted after modifications
 */
public class Knowledge {
    private KnowledgeId id;
    private CategoryId categoryId;
    private String name;
    private String description;
    private ApprovalStatus approvalStatus;
    private Map<String, String> attributes;

    // Private constructor to enforce factory method usage
    private Knowledge(KnowledgeId id, CategoryId categoryId, String name, String description,
                     ApprovalStatus approvalStatus, Map<String, String> attributes) {
        validateCategoryId(categoryId);
        validateName(name);

        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.approvalStatus = approvalStatus != null ? approvalStatus : ApprovalStatus.PENDING;
        this.attributes = new HashMap<>(attributes != null ? attributes : new HashMap<>());
    }

    /**
     * Factory method to create a new Knowledge
     */
    public static Knowledge create(CategoryId categoryId, String name, String description,
                                   ApprovalStatus approvalStatus, Map<String, String> attributes) {
        return new Knowledge(null, categoryId, name, description, approvalStatus, attributes);
    }

    /**
     * Factory method to reconstitute Knowledge from persistence
     */
    public static Knowledge reconstitute(KnowledgeId id, CategoryId categoryId, String name,
                                        String description, ApprovalStatus approvalStatus,
                                        Map<String, String> attributes) {
        return new Knowledge(id, categoryId, name, description, approvalStatus, attributes);
    }

    // Business methods

    public void updateName(String newName) {
        ensureCanBeModified();
        validateName(newName);
        this.name = newName;
    }

    public void updateDescription(String newDescription) {
        ensureCanBeModified();
        this.description = newDescription;
    }

    public void changeCategory(CategoryId newCategoryId) {
        ensureCanBeModified();
        validateCategoryId(newCategoryId);
        this.categoryId = newCategoryId;
    }

    public void approve() {
        if (this.approvalStatus == ApprovalStatus.APPROVED) {
            throw new IllegalStateException("Knowledge is already approved");
        }
        this.approvalStatus = ApprovalStatus.APPROVED;
    }

    public void reject() {
        if (this.approvalStatus == ApprovalStatus.REJECTED) {
            throw new IllegalStateException("Knowledge is already rejected");
        }
        if (this.approvalStatus == ApprovalStatus.APPROVED) {
            throw new IllegalStateException("Cannot reject approved knowledge");
        }
        this.approvalStatus = ApprovalStatus.REJECTED;
    }

    public void resubmit() {
        if (this.approvalStatus != ApprovalStatus.REJECTED) {
            throw new IllegalStateException("Only rejected knowledge can be resubmitted");
        }
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public void addAttribute(String key, String value) {
        ensureCanBeModified();
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Attribute key cannot be null or blank");
        }
        this.attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        ensureCanBeModified();
        this.attributes.remove(key);
    }

    public boolean isApproved() {
        return this.approvalStatus.isApproved();
    }

    public boolean isPending() {
        return this.approvalStatus.isPending();
    }

    public boolean canBeDeleted() {
        return !this.approvalStatus.isApproved();
    }

    // Validation methods

    private void validateCategoryId(CategoryId categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Knowledge must belong to a category");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Knowledge name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Knowledge name cannot exceed 255 characters");
        }
    }

    private void ensureCanBeModified() {
        if (!this.approvalStatus.canBeModified()) {
            throw new IllegalStateException("Approved knowledge cannot be modified");
        }
    }

    // Getters

    public KnowledgeId getId() {
        return id;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes); // Return defensive copy
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    // For infrastructure layer to set ID after persistence
    public void setId(KnowledgeId id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot change Knowledge ID once set");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Knowledge knowledge = (Knowledge) o;
        return Objects.equals(id, knowledge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Knowledge{" +
               "id=" + id +
               ", categoryId=" + categoryId +
               ", name='" + name + '\'' +
               ", approvalStatus=" + approvalStatus +
               '}';
    }
}