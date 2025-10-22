package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.adapter;

import com.pragma.vc.tracker.knowledgecatalog.domain.model.Category;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.CategoryId;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.CategoryRepository;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.JpaCategorySpringRepository;
import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.mapper.CategoryEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaCategoryRepositoryAdapter implements CategoryRepository {
    private final JpaCategorySpringRepository jpaRepository;

    public JpaCategoryRepositoryAdapter(JpaCategorySpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Category save(Category category) {
        return CategoryEntityMapper.toDomain(
            jpaRepository.save(CategoryEntityMapper.toEntity(category))
        );
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return jpaRepository.findById(id.getValue())
            .map(CategoryEntityMapper::toDomain);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(CategoryEntityMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
            .map(CategoryEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(CategoryId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
