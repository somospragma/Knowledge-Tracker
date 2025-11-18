package com.pragma.vc.tracker.usermanagement.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.usermanagement.domain.model.User;
import com.pragma.vc.tracker.usermanagement.domain.model.UserId;
import com.pragma.vc.tracker.usermanagement.domain.repository.UserRepository;
import com.pragma.vc.tracker.usermanagement.infrastructure.persistence.entity.JpaUserEntity;
import com.pragma.vc.tracker.usermanagement.infrastructure.persistence.mapper.UserEntityMapper;
import com.pragma.vc.tracker.usermanagement.infrastructure.persistence.repository.JpaUserRepositoryInterface;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the domain UserRepository port using JPA
 * Translates between domain User and JPA JpaUserEntity
 */
@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepositoryInterface jpaRepository;
    private final UserEntityMapper mapper;

    public JpaUserRepositoryAdapter(JpaUserRepositoryInterface jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        JpaUserEntity entity = mapper.toEntity(user);
        JpaUserEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email.toLowerCase().trim())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email.toLowerCase().trim());
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllActive() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
