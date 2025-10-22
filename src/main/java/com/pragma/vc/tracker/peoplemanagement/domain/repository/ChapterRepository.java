package com.pragma.vc.tracker.peoplemanagement.domain.repository;

import com.pragma.vc.tracker.peoplemanagement.domain.model.Chapter;
import com.pragma.vc.tracker.peoplemanagement.domain.model.ChapterId;

import java.util.List;
import java.util.Optional;

/**
 * Repository Port for Chapter aggregate
 * Domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface ChapterRepository {

    /**
     * Save a new or updated Chapter
     */
    Chapter save(Chapter chapter);

    /**
     * Find a Chapter by its ID
     */
    Optional<Chapter> findById(ChapterId id);

    /**
     * Find a Chapter by its unique name
     */
    Optional<Chapter> findByName(String name);

    /**
     * Find all Chapters
     */
    List<Chapter> findAll();

    /**
     * Check if a Chapter exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Delete a Chapter by ID
     */
    void deleteById(ChapterId id);

    /**
     * Count all Chapters
     */
    long count();
}