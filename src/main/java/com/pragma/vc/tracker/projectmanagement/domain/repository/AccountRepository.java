package com.pragma.vc.tracker.projectmanagement.domain.repository;

import com.pragma.vc.tracker.projectmanagement.domain.model.Account;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountId;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository Port for Account aggregate
 * Domain interface - no framework dependencies
 * Implementation will be in infrastructure layer
 */
public interface AccountRepository {

    /**
     * Save a new or updated Account
     */
    Account save(Account account);

    /**
     * Find an Account by its ID
     */
    Optional<Account> findById(AccountId id);

    /**
     * Find an Account by its unique name
     */
    Optional<Account> findByName(String name);

    /**
     * Find all Accounts
     */
    List<Account> findAll();

    /**
     * Find Accounts by status
     */
    List<Account> findByStatus(AccountStatus status);

    /**
     * Find Accounts by territory
     */
    List<Account> findByTerritory(String territory);

    /**
     * Check if an Account exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Delete an Account by ID
     */
    void deleteById(AccountId id);

    /**
     * Count all Accounts
     */
    long count();
}