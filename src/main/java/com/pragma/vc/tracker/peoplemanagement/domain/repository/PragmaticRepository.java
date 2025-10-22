package com.pragma.vc.tracker.peoplemanagement.domain.repository;

import com.pragma.vc.tracker.peoplemanagement.domain.model.ChapterId;
import com.pragma.vc.tracker.peoplemanagement.domain.model.Email;
import com.pragma.vc.tracker.peoplemanagement.domain.model.Pragmatic;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository Port for Pragmatic aggregate
 * Domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface PragmaticRepository {

    /**
     * Save a new or updated Pragmatic
     */
    Pragmatic save(Pragmatic pragmatic);

    /**
     * Find a Pragmatic by ID
     */
    Optional<Pragmatic> findById(PragmaticId id);

    /**
     * Find a Pragmatic by their unique email
     */
    Optional<Pragmatic> findByEmail(Email email);

    /**
     * Find all Pragmatics
     */
    List<Pragmatic> findAll();

    /**
     * Find Pragmatics by Chapter
     */
    List<Pragmatic> findByChapterId(ChapterId chapterId);

    /**
     * Find Pragmatics by status
     */
    List<Pragmatic> findByStatus(PragmaticStatus status);

    /**
     * Find active Pragmatics in a Chapter
     */
    List<Pragmatic> findActiveByChapterId(ChapterId chapterId);

    /**
     * Check if a Pragmatic exists with the given email
     */
    boolean existsByEmail(Email email);

    /**
     * Delete a Pragmatic by ID
     */
    void deleteById(PragmaticId id);

    /**
     * Count all Pragmatics
     */
    long count();

    /**
     * Count Pragmatics by Chapter
     */
    long countByChapterId(ChapterId chapterId);

    /**
     * Count Pragmatics by status
     */
    long countByStatus(PragmaticStatus status);
}