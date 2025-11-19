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
import com.pragma.vc.tracker.usermanagement.domain.model.UserId;
import com.pragma.vc.tracker.usermanagement.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RegisterUserUseCase.
 * Tests the use case in isolation using mocks for dependencies.
 */
@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KeycloakUserService keycloakUserService;

    @Mock
    private EventPublisher eventPublisher;

    private UserMapper userMapper;

    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        registerUserUseCase = new RegisterUserUseCase(
                userRepository,
                keycloakUserService,
                eventPublisher,
                userMapper
        );
    }

    @Test
    @DisplayName("Should successfully register a new user with all integrations")
    void shouldRegisterUserSuccessfully() {
        // Given
        RegisterUserCommand command = new RegisterUserCommand(
                "john.doe@pragma.com.co",
                "John",
                "Doe",
                "USER",
                "SecurePassword123"
        );

        User savedUser = User.reconstitute(
                UserId.generate(),
                "john.doe@pragma.com.co",
                "John",
                "Doe",
                SystemRole.USER,
                true
        );

        when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(keycloakUserService.createUser(
                command.getEmail(),
                command.getFirstName(),
                command.getLastName(),
                command.getPassword(),
                false
        )).thenReturn("keycloak-user-id-123");

        // When
        UserDTO result = registerUserUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john.doe@pragma.com.co");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getSystemRole()).isEqualTo("USER");
        assertThat(result.isActive()).isTrue();
        assertThat(result.getKeycloakId()).isEqualTo("keycloak-user-id-123");

        // Verify interactions
        verify(userRepository).existsByEmail(command.getEmail());
        verify(userRepository).save(any(User.class));
        verify(keycloakUserService).createUser(
                command.getEmail(),
                command.getFirstName(),
                command.getLastName(),
                command.getPassword(),
                false
        );

        // Verify event was published
        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(eventPublisher).publish(eventCaptor.capture());

        UserRegisteredEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.getEmail()).isEqualTo("john.doe@pragma.com.co");
        assertThat(publishedEvent.getFirstName()).isEqualTo("John");
        assertThat(publishedEvent.getLastName()).isEqualTo("Doe");
        assertThat(publishedEvent.getSystemRole()).isEqualTo(SystemRole.USER);
    }

    @Test
    @DisplayName("Should throw DuplicateUserEmailException when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        RegisterUserCommand command = new RegisterUserCommand(
                "existing@pragma.com.co",
                "Jane",
                "Smith",
                "USER",
                "Password123"
        );

        when(userRepository.existsByEmail(command.getEmail())).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> registerUserUseCase.execute(command))
                .isInstanceOf(DuplicateUserEmailException.class)
                .hasMessageContaining("existing@pragma.com.co");

        // Verify no further interactions
        verify(userRepository).existsByEmail(command.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(keycloakUserService, eventPublisher);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid system role")
    void shouldThrowExceptionForInvalidRole() {
        // Given
        RegisterUserCommand command = new RegisterUserCommand(
                "user@pragma.com.co",
                "John",
                "Doe",
                "INVALID_ROLE",
                "Password123"
        );

        when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);

        // When / Then
        assertThatThrownBy(() -> registerUserUseCase.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid system role");

        verify(userRepository).existsByEmail(command.getEmail());
        verifyNoInteractions(keycloakUserService, eventPublisher);
    }

    @Test
    @DisplayName("Should rollback database changes when Keycloak creation fails")
    void shouldRollbackWhenKeycloakFails() {
        // Given
        RegisterUserCommand command = new RegisterUserCommand(
                "user@pragma.com.co",
                "John",
                "Doe",
                "USER",
                "Password123"
        );

        User savedUser = User.reconstitute(
                UserId.generate(),
                "user@pragma.com.co",
                "John",
                "Doe",
                SystemRole.USER,
                true
        );

        when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(keycloakUserService.createUser(any(), any(), any(), any(), anyBoolean()))
                .thenThrow(new RuntimeException("Keycloak connection error"));

        // When / Then
        assertThatThrownBy(() -> registerUserUseCase.execute(command))
                .isInstanceOf(RegisterUserUseCase.UserRegistrationException.class)
                .hasMessageContaining("Failed to create user in Keycloak");

        // Verify rollback occurred
        verify(userRepository).save(any(User.class));
        verify(userRepository).deleteById(savedUser.getId());

        // Verify event was not published
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("Should register user with temporary password when specified")
    void shouldRegisterUserWithTemporaryPassword() {
        // Given
        RegisterUserCommand command = new RegisterUserCommand(
                "temp.user@pragma.com.co",
                "Temp",
                "User",
                "USER",
                "TempPassword123"
        );
        command.setTemporaryPassword(true);

        User savedUser = User.reconstitute(
                UserId.generate(),
                "temp.user@pragma.com.co",
                "Temp",
                "User",
                SystemRole.USER,
                true
        );

        when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(keycloakUserService.createUser(any(), any(), any(), any(), eq(true)))
                .thenReturn("keycloak-temp-id");

        // When
        UserDTO result = registerUserUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        verify(keycloakUserService).createUser(
                command.getEmail(),
                command.getFirstName(),
                command.getLastName(),
                command.getPassword(),
                true // temporary password
        );
    }

    @Test
    @DisplayName("Should register FULL_ADMINISTRATOR user successfully")
    void shouldRegisterAdministrator() {
        // Given
        RegisterUserCommand command = new RegisterUserCommand(
                "admin@pragma.com.co",
                "Admin",
                "User",
                "FULL_ADMINISTRATOR",
                "AdminPassword123"
        );

        User savedUser = User.reconstitute(
                UserId.generate(),
                "admin@pragma.com.co",
                "Admin",
                "User",
                SystemRole.FULL_ADMINISTRATOR,
                true
        );

        when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(keycloakUserService.createUser(any(), any(), any(), any(), anyBoolean()))
                .thenReturn("keycloak-admin-id");

        // When
        UserDTO result = registerUserUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSystemRole()).isEqualTo("FULL_ADMINISTRATOR");

        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(eventPublisher).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getSystemRole()).isEqualTo(SystemRole.FULL_ADMINISTRATOR);
    }
}
