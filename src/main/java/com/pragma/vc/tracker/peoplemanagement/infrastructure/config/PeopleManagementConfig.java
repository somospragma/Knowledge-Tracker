package com.pragma.vc.tracker.peoplemanagement.infrastructure.config;

import com.pragma.vc.tracker.peoplemanagement.application.usecase.ChapterService;
import com.pragma.vc.tracker.peoplemanagement.application.usecase.PragmaticService;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.ChapterRepository;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.PragmaticRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration for People Management bounded context
 * Wires together the hexagonal architecture layers
 */
@Configuration
public class PeopleManagementConfig {

    @Bean
    public ChapterService chapterService(ChapterRepository chapterRepository) {
        return new ChapterService(chapterRepository);
    }

    @Bean
    public PragmaticService pragmaticService(PragmaticRepository pragmaticRepository,
                                            ChapterRepository chapterRepository) {
        return new PragmaticService(pragmaticRepository, chapterRepository);
    }
}