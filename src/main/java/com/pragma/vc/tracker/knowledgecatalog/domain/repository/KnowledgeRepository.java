package com.pragma.vc.tracker.knowledgecatalog.domain.repository;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.ApprovalStatus;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.CategoryId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.Knowledge;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;

import java.util.List;
import java.util.Optional;

/**
 * Repository Port for Knowledge aggregate
 * Domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface KnowledgeRepository {

    /**
     * Save a new or updated Knowledge
     */
    Knowledge save(Knowledge knowledge);

    /**
     * Find Knowledge by its ID
     */
    Optional<Knowledge> findById(KnowledgeId id);

    /**
     * Find all Knowledge items
     */
    List<Knowledge> findAll();

    /**
     * Find Knowledge by Category
     */
    List<Knowledge> findByCategoryId(CategoryId categoryId);

    /**
     * Find Knowledge by approval status
     */
    List<Knowledge> findByApprovalStatus(ApprovalStatus status);

    /**
     * Find approved Knowledge by Category
     */
    List<Knowledge> findApprovedByCategoryId(CategoryId categoryId);

    /**
     * Search Knowledge by name (case-insensitive, partial match)
     */
    List<Knowledge> searchByName(String name);

    /**
     * Delete Knowledge by ID
     */
    void deleteById(KnowledgeId id);

    /**
     * Count all Knowledge items
     */
    long count();

    /**
     * Count Knowledge by Category
     */
    long countByCategoryId(CategoryId categoryId);

    /**
     * Count Knowledge by approval status
     */
    long countByApprovalStatus(ApprovalStatus status);
}