package com.pragma.vc.tracker.knowledgeapplication.infrastructure.config;

import com.pragma.vc.tracker.knowledgeapplication.application.usecase.AppliedKnowledgeService;
import com.pragma.vc.tracker.knowledgeapplication.domain.repository.AppliedKnowledgeRepository;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.KnowledgeRepository;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.LevelRepository;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.PragmaticRepository;
import com.pragma.vc.tracker.projectmanagement.domain.repository.ProjectRepository;
import com.pragma.vc.tracker.shared.application.port.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnowledgeApplicationConfig {

    @Bean
    public AppliedKnowledgeService appliedKnowledgeService(
            AppliedKnowledgeRepository appliedKnowledgeRepository,
            ProjectRepository projectRepository,
            PragmaticRepository pragmaticRepository,
            KnowledgeRepository knowledgeRepository,
            LevelRepository levelRepository,
            EventPublisher eventPublisher) {
        return new AppliedKnowledgeService(
                appliedKnowledgeRepository,
                projectRepository,
                pragmaticRepository,
                knowledgeRepository,
                levelRepository,
                eventPublisher
        );
    }
}