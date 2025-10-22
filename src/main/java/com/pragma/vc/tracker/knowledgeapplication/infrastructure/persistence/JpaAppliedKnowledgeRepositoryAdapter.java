package com.pragma.vc.tracker.knowledgeapplication.infrastructure.persistence;

import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledge;
import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledgeId;
import com.pragma.vc.tracker.knowledgeapplication.domain.repository.AppliedKnowledgeRepository;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaAppliedKnowledgeRepositoryAdapter implements AppliedKnowledgeRepository {

    private final JpaAppliedKnowledgeSpringRepository springRepository;

    public JpaAppliedKnowledgeRepositoryAdapter(JpaAppliedKnowledgeSpringRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public AppliedKnowledge save(AppliedKnowledge appliedKnowledge) {
        JpaAppliedKnowledgeEntity entity = AppliedKnowledgeEntityMapper.toEntity(appliedKnowledge);
        JpaAppliedKnowledgeEntity saved = springRepository.save(entity);
        return AppliedKnowledgeEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<AppliedKnowledge> findById(AppliedKnowledgeId id) {
        return springRepository.findById(id.getValue())
                .map(AppliedKnowledgeEntityMapper::toDomain);
    }

    @Override
    public List<AppliedKnowledge> findAll() {
        return springRepository.findAll().stream()
                .map(AppliedKnowledgeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppliedKnowledge> findByProjectId(ProjectId projectId) {
        return springRepository.findByProjectId(projectId.getValue()).stream()
                .map(AppliedKnowledgeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppliedKnowledge> findByPragmaticId(PragmaticId pragmaticId) {
        return springRepository.findByPragmaticId(pragmaticId.getValue()).stream()
                .map(AppliedKnowledgeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppliedKnowledge> findByKnowledgeId(KnowledgeId knowledgeId) {
        return springRepository.findByKnowledgeId(knowledgeId.getValue()).stream()
                .map(AppliedKnowledgeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppliedKnowledge> findByProjectIdAndPragmaticId(ProjectId projectId, PragmaticId pragmaticId) {
        return springRepository.findByProjectIdAndPragmaticId(projectId.getValue(), pragmaticId.getValue()).stream()
                .map(AppliedKnowledgeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(AppliedKnowledgeId id) {
        springRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(AppliedKnowledgeId id) {
        return springRepository.existsById(id.getValue());
    }
}