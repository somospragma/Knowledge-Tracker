package com.pragma.vc.tracker.knowledgeapplication.application.mapper;

import com.pragma.vc.tracker.knowledgeapplication.application.dto.AppliedKnowledgeDTO;
import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledge;

public class AppliedKnowledgeMapper {

    public static AppliedKnowledgeDTO toDTO(AppliedKnowledge appliedKnowledge) {
        return new AppliedKnowledgeDTO(
                appliedKnowledge.getId() != null ? appliedKnowledge.getId().getValue() : null,
                appliedKnowledge.getProjectId().getValue(),
                appliedKnowledge.getPragmaticId().getValue(),
                appliedKnowledge.getKnowledgeId() != null ? appliedKnowledge.getKnowledgeId().getValue() : null,
                appliedKnowledge.getLevelId() != null ? appliedKnowledge.getLevelId().getValue() : null,
                appliedKnowledge.getStartDate()
        );
    }
}