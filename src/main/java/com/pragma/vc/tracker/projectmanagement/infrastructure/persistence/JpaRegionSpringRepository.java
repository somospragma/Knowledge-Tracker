package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence;

import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaRegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Region
 * Extends JpaRepository to get basic CRUD operations
 */
public interface JpaRegionSpringRepository extends JpaRepository<JpaRegionEntity, Long> {

    Optional<JpaRegionEntity> findByName(String name);

    boolean existsByName(String name);
}
