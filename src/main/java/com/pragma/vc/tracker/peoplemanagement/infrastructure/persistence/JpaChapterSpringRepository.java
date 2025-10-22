package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence;

import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity.JpaChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Chapter
 * Extends JpaRepository to get basic CRUD operations
 */
public interface JpaChapterSpringRepository extends JpaRepository<JpaChapterEntity, Long> {

    Optional<JpaChapterEntity> findByName(String name);

    boolean existsByName(String name);
}