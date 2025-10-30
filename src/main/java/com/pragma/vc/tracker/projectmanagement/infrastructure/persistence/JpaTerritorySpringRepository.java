package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence;

import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaTerritoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Territory
 * Extends JpaRepository to get basic CRUD operations
 */
public interface JpaTerritorySpringRepository extends JpaRepository<JpaTerritoryEntity, Long> {

    Optional<JpaTerritoryEntity> findByName(String name);

    boolean existsByName(String name);
}
