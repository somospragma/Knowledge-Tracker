package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.*;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.KnowledgeRepository;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.JpaKnowledgeSpringRepository;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.mapper.KnowledgeEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaKnowledgeRepositoryAdapter implements KnowledgeRepository {
    private final JpaKnowledgeSpringRepository jpaRepository;

    public JpaKnowledgeRepositoryAdapter(JpaKnowledgeSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Knowledge save(Knowledge knowledge) {
        return KnowledgeEntityMapper.toDomain(
            jpaRepository.save(KnowledgeEntityMapper.toEntity(knowledge))
        );
    }

    @Override
    public Optional<Knowledge> findById(KnowledgeId id) {
        return jpaRepository.findById(id.getValue())
            .map(KnowledgeEntityMapper::toDomain);
    }

    @Override
    public List<Knowledge> findAll() {
        return jpaRepository.findAll().stream()
            .map(KnowledgeEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Knowledge> findByCategoryId(CategoryId categoryId) {
        return jpaRepository.findByCategoryId(categoryId.getValue()).stream()
            .map(KnowledgeEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Knowledge> findByApprovalStatus(ApprovalStatus status) {
        return jpaRepository.findByApprovalStatus(status.name()).stream()
            .map(KnowledgeEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Knowledge> findApprovedByCategoryId(CategoryId categoryId) {
        return jpaRepository.findByCategoryId(categoryId.getValue()).stream()
            .map(KnowledgeEntityMapper::toDomain)
            .filter(Knowledge::isApproved)
            .collect(Collectors.toList());
    }

    @Override
    public List<Knowledge> searchByName(String name) {
        return jpaRepository.searchByName(name).stream()
            .map(KnowledgeEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(KnowledgeId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByCategoryId(CategoryId categoryId) {
        return jpaRepository.countByCategoryId(categoryId.getValue());
    }

    @Override
    public long countByApprovalStatus(ApprovalStatus status) {
        return jpaRepository.countByApprovalStatus(status.name());
    }
}
