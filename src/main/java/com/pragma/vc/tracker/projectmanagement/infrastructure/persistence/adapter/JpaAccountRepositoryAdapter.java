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
    public List<Account> findByRegion(String region) {
        return jpaRepository.findByRegion(region).stream()
            .map(AccountEntityMapper::toDomain)
            .collect(Collectors.toList());
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