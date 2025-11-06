package com.pragma.vc.tracker.knowledgeapplication.infrastructure.web;

import com.pragma.vc.tracker.knowledgeapplication.application.dto.AppliedKnowledgeDTO;
import com.pragma.vc.tracker.knowledgeapplication.application.dto.CreateAppliedKnowledgeCommand;
import com.pragma.vc.tracker.knowledgeapplication.application.dto.UpdateAppliedKnowledgeCommand;
import com.pragma.vc.tracker.knowledgeapplication.application.usecase.AppliedKnowledgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applied-knowledge")
@Tag(name = "Applied Knowledge (Core)", description = "CORE DOMAIN - Knowledge application tracking API - Record and manage knowledge applied by pragmatics on projects")
public class AppliedKnowledgeController {
    private final AppliedKnowledgeService appliedKnowledgeService;

    public AppliedKnowledgeController(AppliedKnowledgeService appliedKnowledgeService) {
        this.appliedKnowledgeService = appliedKnowledgeService;
    }

    @Operation(summary = "Record knowledge application",
               description = "Records a pragmatic applying specific knowledge on a project at a proficiency level. This is the core business operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Knowledge application recorded successfully",
                    content = @Content(schema = @Schema(implementation = AppliedKnowledgeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AppliedKnowledgeDTO> createAppliedKnowledge(@RequestBody CreateAppliedKnowledgeCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appliedKnowledgeService.createAppliedKnowledge(command));
    }

    @Operation(summary = "Get applied knowledge by ID", description = "Retrieves a specific knowledge application record by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applied knowledge found",
                    content = @Content(schema = @Schema(implementation = AppliedKnowledgeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Applied knowledge not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppliedKnowledgeDTO> getAppliedKnowledgeById(
            @Parameter(description = "Applied Knowledge ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeById(id));
    }

    @Operation(summary = "Get applied knowledge records",
               description = "Retrieves knowledge application records with optional filtering by project, pragmatic, or knowledge item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applied knowledge records retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AppliedKnowledgeDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<AppliedKnowledgeDTO>> getAppliedKnowledge(
            @Parameter(description = "Filter by project ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "Filter by pragmatic (employee) ID") @RequestParam(required = false) Long pragmaticId,
            @Parameter(description = "Filter by knowledge item ID") @RequestParam(required = false) Long knowledgeId) {

        if (projectId != null && pragmaticId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByProjectAndPragmatic(projectId, pragmaticId));
        } else if (projectId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByProjectId(projectId));
        } else if (pragmaticId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByPragmaticId(pragmaticId));
        } else if (knowledgeId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByKnowledgeId(knowledgeId));
        } else {
            return ResponseEntity.ok(appliedKnowledgeService.getAllAppliedKnowledge());
        }
    }

    @Operation(summary = "Update applied knowledge",
               description = "Updates an existing knowledge application record (e.g., change proficiency level, update dates)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applied knowledge updated successfully",
                    content = @Content(schema = @Schema(implementation = AppliedKnowledgeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Applied knowledge not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AppliedKnowledgeDTO> updateAppliedKnowledge(
            @Parameter(description = "Applied Knowledge ID", required = true) @PathVariable Long id,
            @RequestBody UpdateAppliedKnowledgeCommand command) {
        return ResponseEntity.ok(appliedKnowledgeService.updateAppliedKnowledge(id, command));
    }

    @Operation(summary = "Delete applied knowledge", description = "Removes a knowledge application record from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Applied knowledge deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Applied knowledge not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppliedKnowledge(
            @Parameter(description = "Applied Knowledge ID", required = true) @PathVariable Long id) {
        appliedKnowledgeService.deleteAppliedKnowledge(id);
        return ResponseEntity.noContent().build();
    }
}
