package com.pragma.vc.tracker.usermanagement.application.usecase;

import com.pragma.vc.tracker.shared.application.port.EventPublisher;
import com.pragma.vc.tracker.usermanagement.application.dto.RegisterUserCommand;
import com.pragma.vc.tracker.usermanagement.application.dto.UserDTO;
import com.pragma.vc.tracker.usermanagement.application.mapper.UserMapper;
import com.pragma.vc.tracker.usermanagement.application.port.KeycloakUserService;
import com.pragma.vc.tracker.usermanagement.domain.event.UserRegisteredEvent;
import com.pragma.vc.tracker.usermanagement.domain.exception.DuplicateUserEmailException;
import com.pragma.vc.tracker.usermanagement.domain.model.SystemRole;
import com.pragma.vc.tracker.usermanagement.domain.model.User;
import com.pragma.vc.tracker.usermanagement.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use case for registering a new user in the system.
 * This use case orchestrates the following operations:
 * 1. Validate that email is not already registered
 * 2. Create user in local database
 * 3. Create user in Keycloak
 * 4. Publish UserRegisteredEvent
 *
 * Follows hexagonal architecture - this is a plain Java class with no Spring annotations.
 */
public class RegisterUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserUseCase.class);

    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;
    private final EventPublisher eventPublisher;
    private final UserMapper userMapper;

    public RegisterUserUseCase(
            UserRepository userRepository,
            KeycloakUserService keycloakUserService,
            EventPublisher eventPublisher,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.keycloakUserService = keycloakUserService;
        this.eventPublisher = eventPublisher;
        this.userMapper = userMapper;
    }

    /**
     * Registers a new user in the system.
     *
     * @param command Registration command with user details
     * @return UserDTO with the created user information
     * @throws DuplicateUserEmailException if email is already registered
     * @throws IllegalArgumentException if system role is invalid
     */
    public UserDTO execute(RegisterUserCommand command) {
        logger.info("Registering new user with email: {}", command.getEmail());

        // 1. Validate email is not already registered
        if (userRepository.existsByEmail(command.getEmail())) {
            logger.warn("Attempted to register user with duplicate email: {}", command.getEmail());
            throw new DuplicateUserEmailException(command.getEmail());
        }

        // 2. Parse and validate system role
        SystemRole systemRole;
        try {
            systemRole = SystemRole.valueOf(command.getSystemRole());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid system role: {}", command.getSystemRole());
            throw new IllegalArgumentException("Invalid system role: " + command.getSystemRole());
        }

        // 3. Create user in domain model
        User user = User.create(
                command.getEmail(),
                command.getFirstName(),
                command.getLastName(),
                systemRole
        );

        // 4. Save user to database
        User savedUser = userRepository.save(user);
        logger.info("User saved to database with ID: {}", savedUser.getId().getValue());

        // 5. Create user in Keycloak
        String keycloakUserId;
        try {
            keycloakUserId = keycloakUserService.createUser(
                    command.getEmail(),
                    command.getFirstName(),
                    command.getLastName(),
                    command.getPassword(),
                    command.isTemporaryPassword()
            );
            logger.info("User created in Keycloak with ID: {}", keycloakUserId);

            // 5a. Assign system role to user in Keycloak
            try {
                String keycloakRoleName = systemRole.toKeycloakRoleName();
                keycloakUserService.assignRoles(keycloakUserId, keycloakRoleName);
                logger.info("Assigned role '{}' to user {} in Keycloak", keycloakRoleName, keycloakUserId);
            } catch (Exception roleException) {
                // Log warning but don't fail the registration if role assignment fails
                // The user exists in both systems, they just won't have the correct role in Keycloak
                logger.warn("Failed to assign role to user {} in Keycloak: {}. User created but role assignment failed.",
                        keycloakUserId, roleException.getMessage());
                // Note: Consider whether this should be a hard failure or soft failure
                // Current implementation: soft failure (user created, role assignment failed)
            }

        } catch (Exception e) {
            // Rollback: delete user from database if Keycloak creation fails
            logger.error("Failed to create user in Keycloak, rolling back database changes", e);
            userRepository.deleteById(savedUser.getId());
            throw new UserRegistrationException("Failed to create user in Keycloak: " + e.getMessage(), e);
        }

        // 6. Publish UserRegisteredEvent
        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getId().getValue().toString(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getSystemRole()
        );
        eventPublisher.publish(event);
        logger.info("Published UserRegisteredEvent for user: {}", savedUser.getEmail());

        // 7. Convert to DTO and return
        UserDTO userDTO = userMapper.toDTO(savedUser);
        userDTO.setKeycloakId(keycloakUserId);

        logger.info("User registration completed successfully: {}", savedUser.getEmail());
        return userDTO;
    }

    /**
     * Custom exception for user registration failures
     */
    public static class UserRegistrationException extends RuntimeException {
        public UserRegistrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
