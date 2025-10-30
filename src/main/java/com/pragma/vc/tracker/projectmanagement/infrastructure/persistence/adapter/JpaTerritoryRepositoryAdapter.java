package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.projectmanagement.domain.model.Territory;
import com.pragma.vc.tracker.projectmanagement.domain.model.TerritoryId;
import com.pragma.vc.tracker.projectmanagement.domain.repository.TerritoryRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.JpaTerritorySpringRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaTerritoryEntity;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper.TerritoryEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the TerritoryRepository port using Spring Data JPA
 * This is in the infrastructure layer and adapts JPA to our domain
 */
@Repository
public class JpaTerritoryRepositoryAdapter implements TerritoryRepository {

    private final JpaTerritorySpringRepository jpaRepository;

    public JpaTerritoryRepositoryAdapter(JpaTerritorySpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Territory save(Territory territory) {
        JpaTerritoryEntity entity = TerritoryEntityMapper.toEntity(territory);
        JpaTerritoryEntity savedEntity = jpaRepository.save(entity);

        // Set the ID back to the domain object if it was newly created
        if (territory.getId() == null && savedEntity.getId() != null) {
            territory.setId(TerritoryId.of(savedEntity.getId()));
        }

        return TerritoryEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Territory> findById(TerritoryId id) {
        return jpaRepository.findById(id.getValue())
            .map(TerritoryEntityMapper::toDomain);
    }

    @Override
    public Optional<Territory> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(TerritoryEntityMapper::toDomain);
    }

    @Override
    public List<Territory> findAll() {
        return jpaRepository.findAll().stream()
            .map(TerritoryEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(TerritoryId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsById(TerritoryId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
