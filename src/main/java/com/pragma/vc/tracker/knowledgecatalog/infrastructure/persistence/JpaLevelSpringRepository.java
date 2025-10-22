package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence;

import com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity.JpaLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaLevelSpringRepository extends JpaRepository<JpaLevelEntity, Long> {
    Optional<JpaLevelEntity> findByName(String name);
    boolean existsByName(String name);
}
