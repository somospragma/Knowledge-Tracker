package com.pragma.vc.tracker.projectmanagement.application.mapper;

import com.pragma.vc.tracker.projectmanagement.application.dto.ProjectDTO;
import com.pragma.vc.tracker.projectmanagement.domain.model.Project;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectStatus;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectType;

/**
 * Mapper to convert between Project domain model and DTOs
 */
public class ProjectMapper {

    public static ProjectDTO toDTO(Project project) {
        return toDTO(project, null);
    }

    public static ProjectDTO toDTO(Project project, String accountName) {
        if (project == null) {
            return null;
        }
        return new ProjectDTO(
            project.getId() != null ? project.getId().getValue() : null,
            project.getAccountId().getValue(),
            accountName,
            project.getName(),
            project.getStatus().name(),
            project.getDateRange() != null ? project.getDateRange().getStartDate() : null,
            project.getDateRange() != null ? project.getDateRange().getEndDate() : null,
            project.getType().name(),
            project.getAttributes()
        );
    }

    public static ProjectStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return ProjectStatus.ACTIVE;
        }
        try {
            return ProjectStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid project status: " + status);
        }
    }

    public static ProjectType parseType(String type) {
        if (type == null || type.isBlank()) {
            return ProjectType.ABIERTO;
        }
        try {
            return ProjectType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid project type: " + type);
        }
    }
}