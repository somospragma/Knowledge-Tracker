package com.pragma.vc.tracker.knowledgecatalog.application.mapper;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.KnowledgeDTO;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.ApprovalStatus;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.Knowledge;

public class KnowledgeMapper {

    public static KnowledgeDTO toDTO(Knowledge knowledge) {
        return toDTO(knowledge, null);
    }

    public static KnowledgeDTO toDTO(Knowledge knowledge, String categoryName) {
        if (knowledge == null) {
            return null;
        }
        return new KnowledgeDTO(
            knowledge.getId() != null ? knowledge.getId().getValue() : null,
            knowledge.getCategoryId().getValue(),
            categoryName,
            knowledge.getName(),
            knowledge.getDescription(),
            knowledge.getApprovalStatus().name(),
            knowledge.getAttributes()
        );
    }

    public static ApprovalStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return ApprovalStatus.PENDING;
        }
        try {
            return ApprovalStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid approval status: " + status);
        }
    }
}