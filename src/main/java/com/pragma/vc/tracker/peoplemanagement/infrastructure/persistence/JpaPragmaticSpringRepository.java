package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence;

import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity.JpaPragmaticEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Pragmatic
 * Extends JpaRepository to get basic CRUD operations
 */
public interface JpaPragmaticSpringRepository extends JpaRepository<JpaPragmaticEntity, Long> {

    Optional<JpaPragmaticEntity> findByEmail(String email);

    List<JpaPragmaticEntity> findByChapterId(Long chapterId);

    List<JpaPragmaticEntity> findByStatus(String status);

    boolean existsByEmail(String email);

    long countByChapterId(Long chapterId);

    long countByStatus(String status);
}