package com.pragma.vc.tracker.knowledgecatalog.infrastructure.config;

import com.pragma.vc.tracker.knowledgecatalog.application.usecase.*;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnowledgeCatalogConfig {

    @Bean
    public CategoryService categoryService(CategoryRepository categoryRepository) {
        return new CategoryService(categoryRepository);
    }

    @Bean
    public LevelService levelService(LevelRepository levelRepository) {
        return new LevelService(levelRepository);
    }

    @Bean
    public KnowledgeService knowledgeService(KnowledgeRepository knowledgeRepository,
                                            CategoryRepository categoryRepository) {
        return new KnowledgeService(knowledgeRepository, categoryRepository);
    }
}
