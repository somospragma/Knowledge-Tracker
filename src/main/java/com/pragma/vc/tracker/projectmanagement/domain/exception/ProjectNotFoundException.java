package com.pragma.vc.tracker.projectmanagement.domain.exception;

import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;

/**
 * Domain exception thrown when a Project is not found
 */
public class ProjectNotFoundException extends RuntimeException {
    private final ProjectId projectId;

    public ProjectNotFoundException(ProjectId projectId) {
        super("Project not found with id: " + projectId);
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}