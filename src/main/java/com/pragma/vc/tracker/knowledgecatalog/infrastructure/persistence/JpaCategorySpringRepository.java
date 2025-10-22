package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence;

import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity.JpaCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCategorySpringRepository extends JpaRepository<JpaCategoryEntity, Long> {
    Optional<JpaCategoryEntity> findByName(String name);
    boolean existsByName(String name);
}
