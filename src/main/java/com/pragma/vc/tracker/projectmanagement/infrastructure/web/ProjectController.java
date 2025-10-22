package com.pragma.vc.tracker.projectmanagement.infrastructure.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateProjectCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.ProjectDTO;
import com.pragma.vc.tracker.projectmanagement.application.usecase.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Project operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody CreateProjectCommand command) {
        ProjectDTO project = projectService.createProject(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String status) {

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

    @PutMapping("/{id}/name")
    public ResponseEntity<ProjectDTO> updateProjectName(
            @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        ProjectDTO project = projectService.updateProjectName(id, request.getName());
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ProjectDTO> completeProject(@PathVariable Long id) {
        ProjectDTO project = projectService.completeProject(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ProjectDTO> activateProject(@PathVariable Long id) {
        ProjectDTO project = projectService.activateProject(id);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
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