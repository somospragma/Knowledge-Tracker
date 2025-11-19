package com.pragma.vc.tracker.usermanagement.application.port;

/**
 * Port (interface) for Keycloak user management operations.
 * This interface defines the contract for creating and managing users in Keycloak.
 */
public interface KeycloakUserService {

    /**
     * Creates a new user in Keycloak
     *
     * @param email User's email (used as username)
     * @param firstName User's first name
     * @param lastName User's last name
     * @param password User's password
     * @param temporaryPassword Whether the password should be temporary (requires change on first login)
     * @return Keycloak user ID
     */
    String createUser(String email, String firstName, String lastName, String password, boolean temporaryPassword);

    /**
     * Assigns roles to a user in Keycloak
     *
     * @param keycloakUserId Keycloak user ID
     * @param roles Array of role names to assign
     */
    void assignRoles(String keycloakUserId, String... roles);

    /**
     * Enables or disables a user in Keycloak
     *
     * @param keycloakUserId Keycloak user ID
     * @param enabled True to enable, false to disable
     */
    void setUserEnabled(String keycloakUserId, boolean enabled);

    /**
     * Deletes a user from Keycloak
     *
     * @param keycloakUserId Keycloak user ID
     */
    void deleteUser(String keycloakUserId);
}
