package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence;

import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity.JpaKnowledgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaKnowledgeSpringRepository extends JpaRepository<JpaKnowledgeEntity, Long> {
    List<JpaKnowledgeEntity> findByCategoryId(Long categoryId);
    List<JpaKnowledgeEntity> findByApprovalStatus(String approvalStatus);
    
    @Query("SELECT k FROM JpaKnowledgeEntity k WHERE LOWER(k.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<JpaKnowledgeEntity> searchByName(@Param("name") String name);
    
    long countByCategoryId(Long categoryId);
    long countByApprovalStatus(String approvalStatus);
}
