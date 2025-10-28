package com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.projectmanagement.domain.model.Region;
import com.pragma.vc.tracker.projectmanagement.domain.model.RegionId;
import com.pragma.vc.tracker.projectmanagement.domain.repository.RegionRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.JpaRegionSpringRepository;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.entity.JpaRegionEntity;
import com.pragma.vc.tracker.projectmanagement.infrastructure.persistence.mapper.RegionEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the RegionRepository port using Spring Data JPA
 * This is in the infrastructure layer and adapts JPA to our domain
 */
@Repository
public class JpaRegionRepositoryAdapter implements RegionRepository {

    private final JpaRegionSpringRepository jpaRepository;

    public JpaRegionRepositoryAdapter(JpaRegionSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Region save(Region region) {
        JpaRegionEntity entity = RegionEntityMapper.toEntity(region);
        JpaRegionEntity savedEntity = jpaRepository.save(entity);

        // Set the ID back to the domain object if it was newly created
        if (region.getId() == null && savedEntity.getId() != null) {
            region.setId(RegionId.of(savedEntity.getId()));
        }

        return RegionEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Region> findById(RegionId id) {
        return jpaRepository.findById(id.getValue())
            .map(RegionEntityMapper::toDomain);
    }

    @Override
    public Optional<Region> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(RegionEntityMapper::toDomain);
    }

    @Override
    public List<Region> findAll() {
        return jpaRepository.findAll().stream()
            .map(RegionEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(RegionId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsById(RegionId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
