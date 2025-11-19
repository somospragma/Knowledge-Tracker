package com.pragma.vc.tracker.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * Configures OAuth2 resource server with JWT validation from Keycloak.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Security configuration for dev profile.
     * Disables security for easier development and testing.
     */
    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    /**
     * Security configuration for production profile.
     * Enables OAuth2 JWT authentication with Keycloak.
     */
    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/v1/users/register",  // User registration endpoint
                                "/actuator/**",             // Actuator endpoints
                                "/swagger-ui/**",           // Swagger UI
                                "/v3/api-docs/**"           // OpenAPI docs
                        ).permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }
}
