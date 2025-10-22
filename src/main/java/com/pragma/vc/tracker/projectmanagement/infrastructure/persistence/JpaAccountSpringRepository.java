package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence;

import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Account
 * Extends JpaRepository to get basic CRUD operations
 */
public interface JpaAccountSpringRepository extends JpaRepository<JpaAccountEntity, Long> {

    Optional<JpaAccountEntity> findByName(String name);

    List<JpaAccountEntity> findByStatus(String status);

    List<JpaAccountEntity> findByRegion(String region);

    boolean existsByName(String name);
}