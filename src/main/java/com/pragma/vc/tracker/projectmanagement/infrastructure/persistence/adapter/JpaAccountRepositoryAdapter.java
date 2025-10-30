package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.projectmanagement.domain.model.Account;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountId;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountStatus;
import com.pragma.vc.tracker.projectmanagement.domain.repository.AccountRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.JpaAccountSpringRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaAccountEntity;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper.AccountEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the AccountRepository port using JPA
 * This bridges the domain layer with the infrastructure layer
 */
@Repository
public class JpaAccountRepositoryAdapter implements AccountRepository {

    private final JpaAccountSpringRepository jpaRepository;

    public JpaAccountRepositoryAdapter(JpaAccountSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {
        JpaAccountEntity entity = AccountEntityMapper.toEntity(account);
        JpaAccountEntity savedEntity = jpaRepository.save(entity);
        return AccountEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return jpaRepository.findById(id.getValue())
            .map(AccountEntityMapper::toDomain);
    }

    @Override
    public Optional<Account> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(AccountEntityMapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return jpaRepository.findAll().stream()
            .map(AccountEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Account> findByStatus(AccountStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
            .map(AccountEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Account> findByTerritory(String territory) {
        // TODO: This is a temporary workaround. Region is now stored as ID (Long).
        // For now, we try to parse the region string as a Long.
        // A better solution would be to update the domain interface to accept TerritoryId.
        try {
            Long territoryId = Long.parseLong(territory);
            return jpaRepository.findByTerritoryId(territoryId).stream()
                .map(AccountEntityMapper::toDomain)
                .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            // If region is not a number, return empty list
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(AccountId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}