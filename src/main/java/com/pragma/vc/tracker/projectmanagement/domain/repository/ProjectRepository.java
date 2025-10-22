package com.pragma.vc.tracker.projectmanagement.domain.repository;

import com.pragma.vc.tracker.projectmanagement.domain.model.AccountId;
import com.pragma.vc.tracker.projectmanagement.domain.model.Project;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository Port for Project aggregate
 * Domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface ProjectRepository {

    /**
     * Save a new or updated Project
     */
    Project save(Project project);

    /**
     * Find a Project by its ID
     */
    Optional<Project> findById(ProjectId id);

    /**
     * Find all Projects
     */
    List<Project> findAll();

    /**
     * Find Projects by Account
     */
    List<Project> findByAccountId(AccountId accountId);

    /**
     * Find Projects by status
     */
    List<Project> findByStatus(ProjectStatus status);

    /**
     * Find active Projects for an Account
     */
    List<Project> findActiveProjectsByAccountId(AccountId accountId);

    /**
     * Delete a Project by ID
     */
    void deleteById(ProjectId id);

    /**
     * Count all Projects
     */
    long count();

    /**
     * Count Projects by Account
     */
    long countByAccountId(AccountId accountId);
}