package com.pragma.vc.tracker.projectmanagement.infrastructure.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateProjectCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.ProjectDTO;
import com.pragma.vc.tracker.projectmanagement.application.usecase.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Project operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Projects", description = "Client project management API - Manage projects associated with client accounts")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Create a new project", description = "Creates a new project associated with a client account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody CreateProjectCommand command) {
        ProjectDTO project = projectService.createProject(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @Operation(summary = "Get project by ID", description = "Retrieves a specific project by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(
            @Parameter(description = "Project ID", required = true) @PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Get all projects", description = "Retrieves all projects with optional filtering by account or status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @Parameter(description = "Filter by account ID") @RequestParam(required = false) Long accountId,
            @Parameter(description = "Filter by project status (Active/Inactive/Completed)") @RequestParam(required = false) String status) {

        List<ProjectDTO> projects;

        if (accountId != null) {
            projects = projectService.getProjectsByAccountId(accountId);
        } else if (status != null && !status.isBlank()) {
            projects = projectService.getProjectsByStatus(status);
        } else {
            projects = projectService.getAllProjects();
        }

        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Update project name", description = "Updates the name of an existing project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project name updated successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @PutMapping("/{id}/name")
    public ResponseEntity<ProjectDTO> updateProjectName(
            @Parameter(description = "Project ID", required = true) @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        ProjectDTO project = projectService.updateProjectName(id, request.getName());
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Complete project", description = "Marks a project as completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project marked as completed",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @PutMapping("/{id}/complete")
    public ResponseEntity<ProjectDTO> completeProject(
            @Parameter(description = "Project ID", required = true) @PathVariable Long id) {
        ProjectDTO project = projectService.completeProject(id);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Activate project", description = "Activates an inactive or completed project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project activated successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @PutMapping("/{id}/activate")
    public ResponseEntity<ProjectDTO> activateProject(
            @Parameter(description = "Project ID", required = true) @PathVariable Long id) {
        ProjectDTO project = projectService.activateProject(id);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Delete project", description = "Deletes a project from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "Project ID", required = true) @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // Inner class for request body
    public static class UpdateNameRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}