package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.projectmanagement.domain.model.AccountId;
import com.pragma.vc.tracker.projectmanagement.domain.model.Project;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectStatus;
import com.pragma.vc.tracker.projectmanagement.domain.repository.ProjectRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.JpaProjectSpringRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaProjectEntity;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper.ProjectEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the ProjectRepository port using JPA
 * This bridges the domain layer with the infrastructure layer
 */
@Repository
public class JpaProjectRepositoryAdapter implements ProjectRepository {

    private final JpaProjectSpringRepository jpaRepository;

    public JpaProjectRepositoryAdapter(JpaProjectSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Project save(Project project) {
        JpaProjectEntity entity = ProjectEntityMapper.toEntity(project);
        JpaProjectEntity savedEntity = jpaRepository.save(entity);
        return ProjectEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Project> findById(ProjectId id) {
        return jpaRepository.findById(id.getValue())
            .map(ProjectEntityMapper::toDomain);
    }

    @Override
    public List<Project> findAll() {
        return jpaRepository.findAll().stream()
            .map(ProjectEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Project> findByAccountId(AccountId accountId) {
        return jpaRepository.findByAccountId(accountId.getValue()).stream()
            .map(ProjectEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Project> findByStatus(ProjectStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
            .map(ProjectEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Project> findActiveProjectsByAccountId(AccountId accountId) {
        return jpaRepository.findByAccountId(accountId.getValue()).stream()
            .map(ProjectEntityMapper::toDomain)
            .filter(Project::isActive)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(ProjectId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByAccountId(AccountId accountId) {
        return jpaRepository.countByAccountId(accountId.getValue());
    }
}