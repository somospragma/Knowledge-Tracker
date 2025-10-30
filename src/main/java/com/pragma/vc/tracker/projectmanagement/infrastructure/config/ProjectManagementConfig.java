package com.pragma.vc.tracker.projectmanagement.infrastructure.config;

import com.pragma.vc.tracker.projectmanagement.application.usecase.AccountService;
import com.pragma.vc.tracker.projectmanagement.application.usecase.ProjectService;
import com.pragma.vc.tracker.projectmanagement.application.usecase.TerritoryService;
import com.pragma.vc.tracker.projectmanagement.domain.repository.AccountRepository;
import com.pragma.vc.tracker.projectmanagement.domain.repository.ProjectRepository;
import com.pragma.vc.tracker.projectmanagement.domain.repository.TerritoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration for Project Management bounded context
 * Wires together the hexagonal architecture layers
 */
@Configuration
public class ProjectManagementConfig {

    @Bean
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountService(accountRepository);
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectRepository, AccountRepository accountRepository) {
        return new ProjectService(projectRepository, accountRepository);
    }

    @Bean
    public TerritoryService territoryService(TerritoryRepository territoryRepository) {
        return new TerritoryService(territoryRepository);
    }
}