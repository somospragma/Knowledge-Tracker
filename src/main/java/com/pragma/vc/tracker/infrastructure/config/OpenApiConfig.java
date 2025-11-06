package com.pragma.vc.tracker.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 Configuration for Knowledge Tracker API Documentation
 *
 * This configuration sets up Swagger UI and generates interactive API documentation
 * for all REST endpoints in the application.
 *
 * Access documentation at:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI knowledgeTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pragma Knowledge Tracker API")
                        .description("""
                                RESTful API for tracking technical knowledge application across Pragma SA projects.

                                This API enables:
                                - Managing client accounts and projects
                                - Tracking Pragma employees (Pragmatics) and their organizational structure
                                - Cataloging technical knowledge (skills, tools, frameworks, techniques)
                                - Recording knowledge application on projects with proficiency levels

                                **Architecture:** Hexagonal Architecture with Domain-Driven Design

                                **Bounded Contexts:**
                                - Project Management: Accounts, Projects, Territories
                                - People Management: Pragmatics, Chapters, KC-Teams
                                - Knowledge Catalog: Knowledge Items, Categories, Proficiency Levels
                                - Knowledge Application (Core): Applied Knowledge assignments
                                """)
                        .version("v1")
                        .contact(new Contact()
                                .name("Pragma SA - Vigilancia Team")
                                .email("vigilancia@pragma.com.co")
                                .url("https://pragma.com.co"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://pragma.com.co/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("http://localhost:8080/api/v1")
                                .description("Development Server - API v1")));
    }
}
