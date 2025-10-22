package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence;

import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository for Project
 * Extends JpaRepository to get basic CRUD operations
 */
public interface JpaProjectSpringRepository extends JpaRepository<JpaProjectEntity, Long> {

    List<JpaProjectEntity> findByAccountId(Long accountId);

    List<JpaProjectEntity> findByStatus(String status);

    long countByAccountId(Long accountId);
}