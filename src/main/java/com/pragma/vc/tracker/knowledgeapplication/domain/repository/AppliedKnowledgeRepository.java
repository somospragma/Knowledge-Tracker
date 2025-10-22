package com.pragma.vc.tracker.knowledgeapplication.domain.repository;

import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledge;
import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledgeId;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for AppliedKnowledge aggregate
 */
public interface AppliedKnowledgeRepository {
    AppliedKnowledge save(AppliedKnowledge appliedKnowledge);
    Optional<AppliedKnowledge> findById(AppliedKnowledgeId id);
    List<AppliedKnowledge> findAll();
    List<AppliedKnowledge> findByProjectId(ProjectId projectId);
    List<AppliedKnowledge> findByPragmaticId(PragmaticId pragmaticId);
    List<AppliedKnowledge> findByKnowledgeId(KnowledgeId knowledgeId);
    List<AppliedKnowledge> findByProjectIdAndPragmaticId(ProjectId projectId, PragmaticId pragmaticId);
    void delete(AppliedKnowledgeId id);
    boolean existsById(AppliedKnowledgeId id);
}