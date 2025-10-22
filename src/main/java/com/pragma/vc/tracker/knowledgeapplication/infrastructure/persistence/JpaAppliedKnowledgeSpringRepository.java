package com.pragma.vc.tracker.knowledgeapplication.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAppliedKnowledgeSpringRepository extends JpaRepository<JpaAppliedKnowledgeEntity, Long> {
    List<JpaAppliedKnowledgeEntity> findByProjectId(Long projectId);
    List<JpaAppliedKnowledgeEntity> findByPragmaticId(Long pragmaticId);
    List<JpaAppliedKnowledgeEntity> findByKnowledgeId(Long knowledgeId);
    List<JpaAppliedKnowledgeEntity> findByProjectIdAndPragmaticId(Long projectId, Long pragmaticId);
}