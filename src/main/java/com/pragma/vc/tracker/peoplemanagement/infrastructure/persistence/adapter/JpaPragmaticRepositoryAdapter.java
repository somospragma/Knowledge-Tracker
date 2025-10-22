package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.peoplemanagement.domain.model.*;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.PragmaticRepository;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.JpaPragmaticSpringRepository;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity.JpaPragmaticEntity;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.mapper.PragmaticEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the PragmaticRepository port using JPA
 * This bridges the domain layer with the infrastructure layer
 */
@Repository
public class JpaPragmaticRepositoryAdapter implements PragmaticRepository {

    private final JpaPragmaticSpringRepository jpaRepository;

    public JpaPragmaticRepositoryAdapter(JpaPragmaticSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pragmatic save(Pragmatic pragmatic) {
        JpaPragmaticEntity entity = PragmaticEntityMapper.toEntity(pragmatic);
        JpaPragmaticEntity savedEntity = jpaRepository.save(entity);
        return PragmaticEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Pragmatic> findById(PragmaticId id) {
        return jpaRepository.findById(id.getValue())
            .map(PragmaticEntityMapper::toDomain);
    }

    @Override
    public Optional<Pragmatic> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
            .map(PragmaticEntityMapper::toDomain);
    }

    @Override
    public List<Pragmatic> findAll() {
        return jpaRepository.findAll().stream()
            .map(PragmaticEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pragmatic> findByChapterId(ChapterId chapterId) {
        return jpaRepository.findByChapterId(chapterId.getValue()).stream()
            .map(PragmaticEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pragmatic> findByStatus(PragmaticStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
            .map(PragmaticEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pragmatic> findActiveByChapterId(ChapterId chapterId) {
        return jpaRepository.findByChapterId(chapterId.getValue()).stream()
            .map(PragmaticEntityMapper::toDomain)
            .filter(Pragmatic::isActive)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public void deleteById(PragmaticId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByChapterId(ChapterId chapterId) {
        return jpaRepository.countByChapterId(chapterId.getValue());
    }

    @Override
    public long countByStatus(PragmaticStatus status) {
        return jpaRepository.countByStatus(status.name());
    }
}