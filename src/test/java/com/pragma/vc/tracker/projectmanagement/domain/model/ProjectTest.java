package com.pragma.vc.tracker.projectmanagement.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Project domain entity
 * Tests business logic without any framework dependencies
 */
class ProjectTest {

    @Test
    void shouldCreateProjectWithValidData() {
        // Given
        AccountId accountId = AccountId.of(1L);
        String name = "Banking Platform";
        ProjectStatus status = ProjectStatus.ACTIVE;
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        ProjectType type = ProjectType.ABIERTO;
        Map<String, String> attributes = new HashMap<>();
        attributes.put("priority", "high");

        // When
        Project project = Project.create(accountId, name, status, dateRange, type, attributes);

        // Then
        assertNotNull(project);
        assertEquals(accountId, project.getAccountId());
        assertEquals(name, project.getName());
        assertEquals(status, project.getStatus());
        assertEquals(type, project.getType());
        assertEquals("high", project.getAttribute("priority"));
    }

    @Test
    void shouldThrowExceptionWhenAccountIdIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Project.create(null, "Project Name", ProjectStatus.ACTIVE, null, ProjectType.ABIERTO, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Given
        AccountId accountId = AccountId.of(1L);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Project.create(accountId, null, ProjectStatus.ACTIVE, null, ProjectType.ABIERTO, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenActiveProjectHasNoStartDate() {
        // Given
        AccountId accountId = AccountId.of(1L);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Project.create(accountId, "Project", ProjectStatus.ACTIVE, null, ProjectType.ABIERTO, null)
        );
    }

    @Test
    void shouldActivateProject() {
        // Given
        AccountId accountId = AccountId.of(1L);
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        Project project = Project.create(accountId, "Project", ProjectStatus.INACTIVE, dateRange, ProjectType.ABIERTO, null);

        // When
        project.activate();

        // Then
        assertEquals(ProjectStatus.ACTIVE, project.getStatus());
        assertTrue(project.isActive());
    }

    @Test
    void shouldThrowExceptionWhenActivatingProjectWithoutStartDate() {
        // Given
        AccountId accountId = AccountId.of(1L);
        Project project = Project.create(accountId, "Project", ProjectStatus.INACTIVE, null, ProjectType.ABIERTO, null);

        // When & Then
        assertThrows(IllegalStateException.class, project::activate);
    }

    @Test
    void shouldCompleteProject() {
        // Given
        AccountId accountId = AccountId.of(1L);
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        Project project = Project.create(accountId, "Project", ProjectStatus.ACTIVE, dateRange, ProjectType.ABIERTO, null);

        // When
        project.complete();

        // Then
        assertEquals(ProjectStatus.COMPLETED, project.getStatus());
    }

    @Test
    void shouldPutProjectOnHold() {
        // Given
        AccountId accountId = AccountId.of(1L);
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        Project project = Project.create(accountId, "Project", ProjectStatus.ACTIVE, dateRange, ProjectType.ABIERTO, null);

        // When
        project.putOnHold();

        // Then
        assertEquals(ProjectStatus.ON_HOLD, project.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPuttingCompletedProjectOnHold() {
        // Given
        AccountId accountId = AccountId.of(1L);
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        Project project = Project.create(accountId, "Project", ProjectStatus.COMPLETED, dateRange, ProjectType.ABIERTO, null);

        // When & Then
        assertThrows(IllegalStateException.class, project::putOnHold);
    }

    @Test
    void shouldUpdateProjectName() {
        // Given
        AccountId accountId = AccountId.of(1L);
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        Project project = Project.create(accountId, "Old Name", ProjectStatus.ACTIVE, dateRange, ProjectType.ABIERTO, null);
        String newName = "New Name";

        // When
        project.updateName(newName);

        // Then
        assertEquals(newName, project.getName());
    }

    @Test
    void shouldUpdateProjectType() {
        // Given
        AccountId accountId = AccountId.of(1L);
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        Project project = Project.create(accountId, "Project", ProjectStatus.ACTIVE, dateRange, ProjectType.ABIERTO, null);

        // When
        project.updateType(ProjectType.CERRADO);

        // Then
        assertEquals(ProjectType.CERRADO, project.getType());
    }

    @Test
    void shouldDefaultToAbiertoTypeWhenNotSpecified() {
        // Given
        AccountId accountId = AccountId.of(1L);

        // When
        Project project = Project.create(accountId, "Project", ProjectStatus.INACTIVE, null, null, null);

        // Then
        assertEquals(ProjectType.ABIERTO, project.getType());
    }

    @Test
    void shouldAcceptNewAssignmentsWhenActiveOrOnHold() {
        // Given
        AccountId accountId = AccountId.of(1L);
        DateRange dateRange = DateRange.from(LocalDateTime.now());
        Project activeProject = Project.create(accountId, "Active", ProjectStatus.ACTIVE, dateRange, ProjectType.ABIERTO, null);
        Project onHoldProject = Project.create(accountId, "OnHold", ProjectStatus.ON_HOLD, dateRange, ProjectType.ABIERTO, null);
        Project completedProject = Project.create(accountId, "Completed", ProjectStatus.COMPLETED, dateRange, ProjectType.ABIERTO, null);

        // Then
        assertTrue(activeProject.canAcceptNewAssignments());
        assertTrue(onHoldProject.canAcceptNewAssignments());
        assertFalse(completedProject.canAcceptNewAssignments());
    }
}