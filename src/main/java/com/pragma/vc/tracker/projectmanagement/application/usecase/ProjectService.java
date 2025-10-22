package com.pragma.vc.tracker.projectmanagement.application.usecase;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateProjectCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.ProjectDTO;
import com.pragma.vc.tracker.projectmanagement.application.mapper.ProjectMapper;
import com.pragma.vc.tracker.projectmanagement.domain.exception.AccountNotFoundException;
import com.pragma.vc.tracker.projectmanagement.domain.exception.ProjectNotFoundException;
import com.pragma.vc.tracker.projectmanagement.domain.model.*;
import com.pragma.vc.tracker.projectmanagement.domain.repository.AccountRepository;
import com.pragma.vc.tracker.projectmanagement.domain.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service for Project use cases
 * Orchestrates domain operations and coordinates with repositories
 */
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;

    public ProjectService(ProjectRepository projectRepository, AccountRepository accountRepository) {
        this.projectRepository = projectRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new Project
     */
    public ProjectDTO createProject(CreateProjectCommand command) {
        // Verify account exists
        AccountId accountId = AccountId.of(command.getAccountId());
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Create domain entity
        ProjectStatus status = ProjectMapper.parseStatus(command.getStatus());
        ProjectType type = ProjectMapper.parseType(command.getType());
        DateRange dateRange = null;
        if (command.getStartDate() != null) {
            dateRange = DateRange.of(command.getStartDate(), command.getEndDate());
        }

        Project project = Project.create(
            accountId,
            command.getName(),
            status,
            dateRange,
            type,
            command.getAttributes()
        );

        // Save and return
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.toDTO(savedProject, account.getName());
    }

    /**
     * Get a Project by ID
     */
    public ProjectDTO getProjectById(Long id) {
        ProjectId projectId = ProjectId.of(id);
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // Fetch account name for DTO
        Account account = accountRepository.findById(project.getAccountId())
            .orElse(null);
        String accountName = account != null ? account.getName() : null;

        return ProjectMapper.toDTO(project, accountName);
    }

    /**
     * Get all Projects
     */
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
            .map(project -> {
                Account account = accountRepository.findById(project.getAccountId()).orElse(null);
                String accountName = account != null ? account.getName() : null;
                return ProjectMapper.toDTO(project, accountName);
            })
            .collect(Collectors.toList());
    }

    /**
     * Get Projects by Account
     */
    public List<ProjectDTO> getProjectsByAccountId(Long accountId) {
        AccountId accId = AccountId.of(accountId);
        Account account = accountRepository.findById(accId)
            .orElseThrow(() -> new AccountNotFoundException(accId));

        return projectRepository.findByAccountId(accId).stream()
            .map(project -> ProjectMapper.toDTO(project, account.getName()))
            .collect(Collectors.toList());
    }

    /**
     * Get Projects by status
     */
    public List<ProjectDTO> getProjectsByStatus(String status) {
        ProjectStatus projectStatus = ProjectMapper.parseStatus(status);
        return projectRepository.findByStatus(projectStatus).stream()
            .map(project -> {
                Account account = accountRepository.findById(project.getAccountId()).orElse(null);
                String accountName = account != null ? account.getName() : null;
                return ProjectMapper.toDTO(project, accountName);
            })
            .collect(Collectors.toList());
    }

    /**
     * Update Project name
     */
    public ProjectDTO updateProjectName(Long id, String newName) {
        ProjectId projectId = ProjectId.of(id);
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.updateName(newName);
        Project updatedProject = projectRepository.save(project);

        Account account = accountRepository.findById(project.getAccountId()).orElse(null);
        String accountName = account != null ? account.getName() : null;

        return ProjectMapper.toDTO(updatedProject, accountName);
    }

    /**
     * Complete a Project
     */
    public ProjectDTO completeProject(Long id) {
        ProjectId projectId = ProjectId.of(id);
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.complete();
        Project updatedProject = projectRepository.save(project);

        Account account = accountRepository.findById(project.getAccountId()).orElse(null);
        String accountName = account != null ? account.getName() : null;

        return ProjectMapper.toDTO(updatedProject, accountName);
    }

    /**
     * Activate a Project
     */
    public ProjectDTO activateProject(Long id) {
        ProjectId projectId = ProjectId.of(id);
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.activate();
        Project updatedProject = projectRepository.save(project);

        Account account = accountRepository.findById(project.getAccountId()).orElse(null);
        String accountName = account != null ? account.getName() : null;

        return ProjectMapper.toDTO(updatedProject, accountName);
    }

    /**
     * Delete a Project
     */
    public void deleteProject(Long id) {
        ProjectId projectId = ProjectId.of(id);
        if (!projectRepository.findById(projectId).isPresent()) {
            throw new ProjectNotFoundException(projectId);
        }
        projectRepository.deleteById(projectId);
    }
}