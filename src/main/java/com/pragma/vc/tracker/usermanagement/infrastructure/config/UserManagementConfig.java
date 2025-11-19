package com.pragma.vc.tracker.usermanagement.infrastructure.config;

import com.pragma.vc.tracker.shared.application.port.EventPublisher;
import com.pragma.vc.tracker.usermanagement.application.mapper.UserMapper;
import com.pragma.vc.tracker.usermanagement.application.port.KeycloakUserService;
import com.pragma.vc.tracker.usermanagement.application.usecase.RegisterUserUseCase;
import com.pragma.vc.tracker.usermanagement.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration for User Management bounded context.
 * Wires together the hexagonal architecture layers.
 *
 * Following hexagonal architecture principles:
 * - Application layer services are plain Java classes (no Spring annotations)
 * - This configuration class registers them as Spring beans
 * - Dependencies are injected via constructor
 */
@Configuration
public class UserManagementConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepository userRepository,
            KeycloakUserService keycloakUserService,
            EventPublisher eventPublisher,
            UserMapper userMapper
    ) {
        return new RegisterUserUseCase(userRepository, keycloakUserService, eventPublisher, userMapper);
    }
}
