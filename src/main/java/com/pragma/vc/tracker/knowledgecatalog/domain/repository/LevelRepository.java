package com.pragma.vc.tracker.knowledgecatalog.domain.repository;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.Level;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;

import java.util.List;
import java.util.Optional;

/**
 * Repository Port for Level aggregate
 * Domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface LevelRepository {

    /**
     * Save a new or updated Level
     */
    Level save(Level level);

    /**
     * Find a Level by its ID
     */
    Optional<Level> findById(LevelId id);

    /**
     * Find a Level by its unique name
     */
    Optional<Level> findByName(String name);

    /**
     * Find all Levels
     */
    List<Level> findAll();

    /**
     * Check if a Level exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Delete a Level by ID
     */
    void deleteById(LevelId id);

    /**
     * Count all Levels
     */
    long count();
}