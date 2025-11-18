package com.pragma.vc.tracker.usermanagement.infrastructure.persistence.repository;

import com.pragma.vc.tracker.usermanagement.infrastructure.persistence.entity.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository interface for User entity
 * Spring will automatically implement this at runtime
 */
@Repository
public interface JpaUserRepositoryInterface extends JpaRepository<JpaUserEntity, UUID> {

    /**
     * Find a user by email address
     */
    Optional<JpaUserEntity> findByEmail(String email);

    /**
     * Check if a user exists with the given email
     */
    boolean existsByEmail(String email);

    /**
     * Find all active users
     */
    List<JpaUserEntity> findByActiveTrue();
}
