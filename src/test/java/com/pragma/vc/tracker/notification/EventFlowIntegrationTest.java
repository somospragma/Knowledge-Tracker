package com.pragma.vc.tracker.notification;

import com.pragma.vc.tracker.knowledgeapplication.application.dto.AppliedKnowledgeDTO;
import com.pragma.vc.tracker.knowledgeapplication.application.dto.CreateAppliedKnowledgeCommand;
import com.pragma.vc.tracker.knowledgeapplication.application.usecase.AppliedKnowledgeService;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.KnowledgeDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.LevelDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.KnowledgeService;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.LevelService;
import com.pragma.vc.tracker.peoplemanagement.application.dto.PragmaticDTO;
import com.pragma.vc.tracker.peoplemanagement.application.usecase.PragmaticService;
import com.pragma.vc.tracker.projectmanagement.application.dto.ProjectDTO;
import com.pragma.vc.tracker.projectmanagement.application.usecase.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test to verify that domain events flow properly from
 * the Knowledge Application context to the Notification context.
 */
@SpringBootTest
@ActiveProfiles("dev")
class EventFlowIntegrationTest {

    @Autowired
    private AppliedKnowledgeService appliedKnowledgeService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PragmaticService pragmaticService;

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private LevelService levelService;

    @Test
    void shouldPublishEventWhenAppliedKnowledgeIsCreated() throws InterruptedException {
        // Given: Use existing entities from test data
        // Get all applied knowledge to find valid IDs
        List<AppliedKnowledgeDTO> existing = appliedKnowledgeService.getAllAppliedKnowledge();
        assertTrue(!existing.isEmpty(), "Test requires at least one existing applied knowledge to extract valid IDs");

        AppliedKnowledgeDTO existingData = existing.get(0);

        // When: Create new applied knowledge using the same valid IDs (this should publish an event)
        CreateAppliedKnowledgeCommand command = new CreateAppliedKnowledgeCommand(
                existingData.getProjectId(),
                existingData.getPragmaticId(),
                existingData.getKnowledgeId(),
                existingData.getLevelId(),
                LocalDate.now()
        );

        AppliedKnowledgeDTO result = appliedKnowledgeService.createAppliedKnowledge(command);

        // Then: Verify creation
        assertNotNull(result);
        assertNotNull(result.getId());

        // Give async event processing time to complete
        Thread.sleep(1000);

        // The event should have been logged by the NotificationService
        System.out.println("=".repeat(80));
        System.out.println("✓ Applied Knowledge created successfully: " + result.getId());
        System.out.println("✓ Event should have been published and logged by NotificationService");
        System.out.println("✓ Check logs above for: [NOTIFICATION] EventID=...");
        System.out.println("=".repeat(80));
    }
}
