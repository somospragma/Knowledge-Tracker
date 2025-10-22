package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.Level;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.LevelRepository;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.JpaLevelSpringRepository;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.mapper.LevelEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaLevelRepositoryAdapter implements LevelRepository {
    private final JpaLevelSpringRepository jpaRepository;

    public JpaLevelRepositoryAdapter(JpaLevelSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Level save(Level level) {
        return LevelEntityMapper.toDomain(
            jpaRepository.save(LevelEntityMapper.toEntity(level))
        );
    }

    @Override
    public Optional<Level> findById(LevelId id) {
        return jpaRepository.findById(id.getValue())
            .map(LevelEntityMapper::toDomain);
    }

    @Override
    public Optional<Level> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(LevelEntityMapper::toDomain);
    }

    @Override
    public List<Level> findAll() {
        return jpaRepository.findAll().stream()
            .map(LevelEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(LevelId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
