package com.pragma.vc.tracker.usermanagement.infrastructure.adapter.keycloak;

import com.pragma.vc.tracker.shared.infrastructure.config.KeycloakProperties;
import com.pragma.vc.tracker.usermanagement.application.port.KeycloakUserService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Adapter implementation for Keycloak user management.
 * Implements the KeycloakUserService port using Keycloak Admin Client.
 */
@Component
public class KeycloakUserServiceAdapter implements KeycloakUserService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserServiceAdapter.class);

    private final KeycloakProperties keycloakProperties;
    private final Keycloak keycloakAdminClient;

    public KeycloakUserServiceAdapter(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakAdminClient = buildKeycloakClient();
    }

    private Keycloak buildKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServerUrl())
                .realm("master") // Admin client uses master realm
                .clientId(keycloakProperties.getAdminClientId())
                .username(keycloakProperties.getAdminUsername())
                .password(keycloakProperties.getAdminPassword())
                .build();
    }

    @Override
    public String createUser(String email, String firstName, String lastName, String password, boolean temporaryPassword) {
        logger.info("Creating user in Keycloak: {}", email);

        RealmResource realmResource = getRealmResource();
        UsersResource usersResource = realmResource.users();

        // Create user representation
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(email);
        userRepresentation.setEmail(email);
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true); // Auto-verify for now

        // Create user
        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() == 201) {
                String locationHeader = response.getHeaderString("Location");
                String keycloakUserId = extractUserIdFromLocation(locationHeader);
                logger.info("User created successfully in Keycloak with ID: {}", keycloakUserId);

                // Set password
                setPassword(keycloakUserId, password, temporaryPassword);

                return keycloakUserId;
            } else {
                String errorMessage = response.readEntity(String.class);
                logger.error("Failed to create user in Keycloak. Status: {}, Error: {}", response.getStatus(), errorMessage);
                throw new KeycloakUserCreationException("Failed to create user in Keycloak: " + errorMessage);
            }
        } catch (Exception e) {
            logger.error("Error creating user in Keycloak", e);
            throw new KeycloakUserCreationException("Error creating user in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public void assignRoles(String keycloakUserId, String... roles) {
        logger.info("Assigning roles to user {} in Keycloak: {}", keycloakUserId, String.join(", ", roles));

        if (roles == null || roles.length == 0) {
            logger.warn("No roles provided for assignment");
            return;
        }

        RealmResource realmResource = getRealmResource();
        UsersResource usersResource = realmResource.users();

        try {
            // Get all available realm roles
            var availableRoles = realmResource.roles().list();

            // Filter to only the roles we want to assign
            var rolesToAssign = availableRoles.stream()
                    .filter(role -> {
                        for (String roleName : roles) {
                            if (role.getName().equalsIgnoreCase(roleName)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .toList();

            if (rolesToAssign.isEmpty()) {
                logger.warn("None of the specified roles were found in Keycloak realm: {}", String.join(", ", roles));
                return;
            }

            // Assign the roles to the user
            usersResource.get(keycloakUserId).roles().realmLevel().add(rolesToAssign);

            logger.info("Successfully assigned {} role(s) to user {}: {}",
                    rolesToAssign.size(),
                    keycloakUserId,
                    rolesToAssign.stream().map(org.keycloak.representations.idm.RoleRepresentation::getName).toList());

        } catch (Exception e) {
            logger.error("Failed to assign roles to user {} in Keycloak", keycloakUserId, e);
            throw new KeycloakRoleAssignmentException("Failed to assign roles in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public void setUserEnabled(String keycloakUserId, boolean enabled) {
        logger.info("Setting user {} enabled status to: {}", keycloakUserId, enabled);

        RealmResource realmResource = getRealmResource();
        UsersResource usersResource = realmResource.users();

        UserRepresentation userRepresentation = usersResource.get(keycloakUserId).toRepresentation();
        userRepresentation.setEnabled(enabled);
        usersResource.get(keycloakUserId).update(userRepresentation);

        logger.info("User enabled status updated successfully");
    }

    @Override
    public void deleteUser(String keycloakUserId) {
        logger.info("Deleting user from Keycloak: {}", keycloakUserId);

        RealmResource realmResource = getRealmResource();
        UsersResource usersResource = realmResource.users();

        usersResource.delete(keycloakUserId);

        logger.info("User deleted successfully from Keycloak");
    }

    private void setPassword(String keycloakUserId, String password, boolean temporaryPassword) {
        logger.debug("Setting password for user: {}", keycloakUserId);

        RealmResource realmResource = getRealmResource();
        UsersResource usersResource = realmResource.users();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(temporaryPassword);

        usersResource.get(keycloakUserId).resetPassword(credential);

        logger.debug("Password set successfully (temporary: {})", temporaryPassword);
    }

    private RealmResource getRealmResource() {
        return keycloakAdminClient.realm(keycloakProperties.getRealm());
    }

    private String extractUserIdFromLocation(String locationHeader) {
        if (locationHeader == null || locationHeader.isEmpty()) {
            throw new KeycloakUserCreationException("Location header not found in response");
        }

        // Location header format: http://keycloak:8080/admin/realms/{realm}/users/{userId}
        String[] parts = locationHeader.split("/");
        return parts[parts.length - 1];
    }

    /**
     * Custom exception for Keycloak user creation failures
     */
    public static class KeycloakUserCreationException extends RuntimeException {
        public KeycloakUserCreationException(String message) {
            super(message);
        }

        public KeycloakUserCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Custom exception for Keycloak role assignment failures
     */
    public static class KeycloakRoleAssignmentException extends RuntimeException {
        public KeycloakRoleAssignmentException(String message) {
            super(message);
        }

        public KeycloakRoleAssignmentException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
