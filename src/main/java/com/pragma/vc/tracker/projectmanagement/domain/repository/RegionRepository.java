package com.pragma.vc.tracker.projectmanagement.domain.repository;

import com.pragma.vc.tracker.projectmanagement.domain.model.Region;
import com.pragma.vc.tracker.projectmanagement.domain.model.RegionId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Region aggregate (Port)
 * This is a domain interface that will be implemented in the infrastructure layer
 */
public interface RegionRepository {

    /**
     * Save a Region (create or update)
     */
    Region save(Region region);

    /**
     * Find a Region by its ID
     */
    Optional<Region> findById(RegionId id);

    /**
     * Find a Region by its name
     */
    Optional<Region> findByName(String name);

    /**
     * Find all Regions
     */
    List<Region> findAll();

    /**
     * Delete a Region by its ID
     */
    void deleteById(RegionId id);

    /**
     * Check if a Region exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Check if a Region exists with the given ID
     */
    boolean existsById(RegionId id);
}
