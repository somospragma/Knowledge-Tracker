package com.pragma.vc.tracker.usermanagement.domain.repository;

import com.pragma.vc.tracker.usermanagement.domain.model.User;
import com.pragma.vc.tracker.usermanagement.domain.model.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Repository port (interface) for User aggregate
 * This is a domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface UserRepository {

    /**
     * Save a new user or update an existing one
     * @param user the user to save
     * @return the saved user with ID assigned
     */
    User save(User user);

    /**
     * Find a user by their unique ID
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> findById(UserId id);

    /**
     * Find a user by their email address (unique)
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user with the given email exists
     * @param email the email address to check
     * @return true if a user with this email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all users in the system
     * @return list of all users
     */
    List<User> findAll();

    /**
     * Find all active users
     * @return list of active users
     */
    List<User> findAllActive();

    /**
     * Delete a user by ID
     * @param id the user ID
     */
    void deleteById(UserId id);

    /**
     * Count total number of users
     * @return total count
     */
    long count();
}
