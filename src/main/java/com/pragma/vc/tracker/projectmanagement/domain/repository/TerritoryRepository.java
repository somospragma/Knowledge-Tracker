package com.pragma.vc.tracker.projectmanagement.domain.repository;

import com.pragma.vc.tracker.projectmanagement.domain.model.Territory;
import com.pragma.vc.tracker.projectmanagement.domain.model.TerritoryId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Territory aggregate (Port)
 * This is a domain interface that will be implemented in the infrastructure layer
 */
public interface TerritoryRepository {

    /**
     * Save a Territory (create or update)
     */
    Territory save(Territory territory);

    /**
     * Find a Territory by its ID
     */
    Optional<Territory> findById(TerritoryId id);

    /**
     * Find a Territory by its name
     */
    Optional<Territory> findByName(String name);

    /**
     * Find all Territories
     */
    List<Territory> findAll();

    /**
     * Delete a Territory by its ID
     */
    void deleteById(TerritoryId id);

    /**
     * Check if a Territory exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Check if a Territory exists with the given ID
     */
    boolean existsById(TerritoryId id);
}
