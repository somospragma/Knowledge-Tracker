package com.pragma.vc.tracker.knowledgecatalog.domain.repository;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.Category;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.CategoryId;

import java.util.List;
import java.util.Optional;

/**
 * Repository Port for Category aggregate
 * Domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface CategoryRepository {

    /**
     * Save a new or updated Category
     */
    Category save(Category category);

    /**
     * Find a Category by its ID
     */
    Optional<Category> findById(CategoryId id);

    /**
     * Find a Category by its unique name
     */
    Optional<Category> findByName(String name);

    /**
     * Find all Categories
     */
    List<Category> findAll();

    /**
     * Check if a Category exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Delete a Category by ID
     */
    void deleteById(CategoryId id);

    /**
     * Count all Categories
     */
    long count();
}