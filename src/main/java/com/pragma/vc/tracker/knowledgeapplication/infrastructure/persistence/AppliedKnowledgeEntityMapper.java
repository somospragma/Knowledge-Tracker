package com.pragma.vc.tracker.knowledgeapplication.infrastructure.persistence;

import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledge;
import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledgeId;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;

public class AppliedKnowledgeEntityMapper {

    public static JpaAppliedKnowledgeEntity toEntity(AppliedKnowledge appliedKnowledge) {
        JpaAppliedKnowledgeEntity entity = new JpaAppliedKnowledgeEntity();

        if (appliedKnowledge.getId() != null) {
            entity.setId(appliedKnowledge.getId().getValue());
        }

        entity.setProjectId(appliedKnowledge.getProjectId().getValue());
        entity.setPragmaticId(appliedKnowledge.getPragmaticId().getValue());

        if (appliedKnowledge.getKnowledgeId() != null) {
            entity.setKnowledgeId(appliedKnowledge.getKnowledgeId().getValue());
        }

        if (appliedKnowledge.getLevelId() != null) {
            entity.setKnowledgeLevel(appliedKnowledge.getLevelId().getValue());
        }

        entity.setOnboardDate(appliedKnowledge.getStartDate());

        return entity;
    }

    public static AppliedKnowledge toDomain(JpaAppliedKnowledgeEntity entity) {
        KnowledgeId knowledgeId = entity.getKnowledgeId() != null ?
                KnowledgeId.of(entity.getKnowledgeId()) : null;

        LevelId levelId = entity.getKnowledgeLevel() != null ?
                LevelId.of(entity.getKnowledgeLevel()) : null;

        AppliedKnowledge appliedKnowledge = AppliedKnowledge.reconstitute(
                AppliedKnowledgeId.of(entity.getId()),
                ProjectId.of(entity.getProjectId()),
                PragmaticId.of(entity.getPragmaticId()),
                knowledgeId,
                levelId,
                entity.getOnboardDate()
        );

        return appliedKnowledge;
    }
}