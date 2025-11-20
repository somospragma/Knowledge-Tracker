package com.pragma.vc.tracker.usermanagement.domain.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Value Object representing System Roles for access control
 * Defines what operations users can perform in the system
 */
public enum SystemRole {
    FULL_ADMINISTRATOR(
        "Full Administrator",
        "Complete system access - can perform all operations",
        Set.of(
            Permission.MANAGE_USERS,
            Permission.CREATE_KNOWLEDGE,
            Permission.UPDATE_KNOWLEDGE,
            Permission.CREATE_APPLIED_KNOWLEDGE,
            Permission.UPDATE_APPLIED_KNOWLEDGE,
            Permission.CREATE_PROJECTS,
            Permission.UPDATE_PROJECTS,
            Permission.CREATE_ACCOUNTS,
            Permission.UPDATE_ACCOUNTS,
            Permission.CREATE_PRAGMATICS,
            Permission.UPDATE_PRAGMATICS,
            Permission.VIEW_ALL
        )
    ),
    KNOWLEDGE_MANAGER(
        "Knowledge Manager",
        "Can create and manage knowledge items and applied knowledge",
        Set.of(
            Permission.CREATE_KNOWLEDGE,
            Permission.UPDATE_KNOWLEDGE,
            Permission.CREATE_APPLIED_KNOWLEDGE,
            Permission.UPDATE_APPLIED_KNOWLEDGE,
            Permission.VIEW_ALL
        )
    ),
    PROJECT_ACCOUNT_MANAGER(
        "Project/Account Manager",
        "Can create and manage projects and client accounts",
        Set.of(
            Permission.CREATE_PROJECTS,
            Permission.UPDATE_PROJECTS,
            Permission.CREATE_ACCOUNTS,
            Permission.UPDATE_ACCOUNTS,
            Permission.VIEW_ALL
        )
    ),
    PEOPLE_MANAGER(
        "People Manager",
        "Can create and update Pragmatics (employees)",
        Set.of(
            Permission.CREATE_PRAGMATICS,
            Permission.UPDATE_PRAGMATICS,
            Permission.VIEW_ALL
        )
    ),
    USER(
        "User",
        "Default role - read-only access for queries",
        Set.of(Permission.VIEW_ALL)
    );

    private final String displayName;
    private final String description;
    private final Set<Permission> permissions;

    SystemRole(String displayName, String description, Set<Permission> permissions) {
        this.displayName = displayName;
        this.description = description;
        this.permissions = permissions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Set<Permission> getPermissions() {
        return Set.copyOf(permissions); // Return immutable copy
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public boolean isAdministrator() {
        return this == FULL_ADMINISTRATOR;
    }

    public boolean canManageKnowledge() {
        return this == FULL_ADMINISTRATOR || this == KNOWLEDGE_MANAGER;
    }

    public boolean canManageProjects() {
        return this == FULL_ADMINISTRATOR || this == PROJECT_ACCOUNT_MANAGER;
    }

    public boolean canManagePeople() {
        return this == FULL_ADMINISTRATOR || this == PEOPLE_MANAGER;
    }

    public static SystemRole fromString(String value) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid system role: " + value));
    }

    /**
     * Converts SystemRole to Keycloak realm role name.
     * Keycloak uses lowercase with underscores for role names.
     *
     * @return Keycloak role name (e.g., "full_administrator", "knowledge_manager")
     */
    public String toKeycloakRoleName() {
        return this.name().toLowerCase();
    }

    /**
     * Gets all Keycloak role names for mapping purposes
     *
     * @return Set of all Keycloak role names
     */
    public static Set<String> getAllKeycloakRoleNames() {
        return Arrays.stream(values())
                .map(SystemRole::toKeycloakRoleName)
                .collect(Collectors.toSet());
    }

    /**
     * Permissions that can be granted through roles
     */
    public enum Permission {
        // User management
        MANAGE_USERS,

        // Knowledge catalog
        CREATE_KNOWLEDGE,
        UPDATE_KNOWLEDGE,

        // Applied knowledge
        CREATE_APPLIED_KNOWLEDGE,
        UPDATE_APPLIED_KNOWLEDGE,

        // Project management
        CREATE_PROJECTS,
        UPDATE_PROJECTS,
        CREATE_ACCOUNTS,
        UPDATE_ACCOUNTS,

        // People management
        CREATE_PRAGMATICS,
        UPDATE_PRAGMATICS,

        // Read access
        VIEW_ALL
    }
}
