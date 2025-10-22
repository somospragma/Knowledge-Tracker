package com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.peoplemanagement.domain.model.Chapter;
import com.pragma.vc.tracker.peoplemanagement.domain.model.ChapterId;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.ChapterRepository;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.JpaChapterSpringRepository;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.entity.JpaChapterEntity;
import com.pragma.vc.tracker.peoplemanagement.infrastructure.persistence.mapper.ChapterEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the ChapterRepository port using JPA
 * This bridges the domain layer with the infrastructure layer
 */
@Repository
public class JpaChapterRepositoryAdapter implements ChapterRepository {

    private final JpaChapterSpringRepository jpaRepository;

    public JpaChapterRepositoryAdapter(JpaChapterSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Chapter save(Chapter chapter) {
        JpaChapterEntity entity = ChapterEntityMapper.toEntity(chapter);
        JpaChapterEntity savedEntity = jpaRepository.save(entity);
        return ChapterEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Chapter> findById(ChapterId id) {
        return jpaRepository.findById(id.getValue())
            .map(ChapterEntityMapper::toDomain);
    }

    @Override
    public Optional<Chapter> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(ChapterEntityMapper::toDomain);
    }

    @Override
    public List<Chapter> findAll() {
        return jpaRepository.findAll().stream()
            .map(ChapterEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(ChapterId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}