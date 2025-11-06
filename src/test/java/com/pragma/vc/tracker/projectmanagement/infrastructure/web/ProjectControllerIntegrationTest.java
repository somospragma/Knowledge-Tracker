package com.pragma.vc.tracker.projectmanagement.infrastructure.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.ProjectDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for ProjectController
 * Tests the REST API endpoints with real Spring context and database
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class ProjectControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllProjects_shouldReturnAllProjects() {
        // Act
        ResponseEntity<List<ProjectDTO>> response = restTemplate.exchange(
                "/api/v1/projects",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectDTO>>() {}
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();

        // Verify at least one project is returned
        List<ProjectDTO> projects = response.getBody();
        assertThat(projects.size()).isGreaterThan(0);

        // Verify first project has required fields
        ProjectDTO firstProject = projects.get(0);
        assertThat(firstProject.getId()).isNotNull();
        assertThat(firstProject.getName()).isNotBlank();
        assertThat(firstProject.getStatus()).isNotNull();
        assertThat(firstProject.getAccountId()).isNotNull();
    }

    @Test
    void testGetAllProjects_withStatusFilter_shouldReturnFilteredProjects() {
        // Act
        ResponseEntity<List<ProjectDTO>> response = restTemplate.exchange(
                "/api/v1/projects?status=Active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectDTO>>() {}
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Verify all returned projects have Active status
        List<ProjectDTO> projects = response.getBody();
        projects.forEach(project ->
            assertThat(project.getStatus()).isEqualTo("Active")
        );
    }

    @Test
    void testGetAllProjects_withAccountIdFilter_shouldReturnFilteredProjects() {
        // Arrange - Get all projects first to find a valid account ID
        ResponseEntity<List<ProjectDTO>> allProjectsResponse = restTemplate.exchange(
                "/api/v1/projects",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectDTO>>() {}
        );

        assertThat(allProjectsResponse.getBody()).isNotEmpty();
        Long accountId = allProjectsResponse.getBody().get(0).getAccountId();

        // Act
        ResponseEntity<List<ProjectDTO>> response = restTemplate.exchange(
                "/api/v1/projects?accountId=" + accountId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectDTO>>() {}
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Verify all returned projects belong to the specified account
        List<ProjectDTO> projects = response.getBody();
        projects.forEach(project ->
            assertThat(project.getAccountId()).isEqualTo(accountId)
        );
    }

    @Test
    void testGetProjectById_shouldReturnProject() {
        // Arrange - Get all projects to find a valid ID
        ResponseEntity<List<ProjectDTO>> allProjectsResponse = restTemplate.exchange(
                "/api/v1/projects",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectDTO>>() {}
        );

        assertThat(allProjectsResponse.getBody()).isNotEmpty();
        Long projectId = allProjectsResponse.getBody().get(0).getId();

        // Act
        ResponseEntity<ProjectDTO> response = restTemplate.getForEntity(
                "/api/v1/projects/" + projectId,
                ProjectDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(projectId);
    }

    @Test
    void testGetProjectById_withInvalidId_shouldReturn404() {
        // Act
        ResponseEntity<ProjectDTO> response = restTemplate.getForEntity(
                "/api/v1/projects/999999",
                ProjectDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
