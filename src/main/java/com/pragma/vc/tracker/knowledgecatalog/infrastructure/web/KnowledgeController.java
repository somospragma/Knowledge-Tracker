package com.pragma.vc.tracker.knowledgecatalog.infrastructure.web;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateKnowledgeCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.KnowledgeDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.KnowledgeService;
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
@RequestMapping("/api/v1/knowledge")
@Tag(name = "Knowledge Catalog", description = "Technical knowledge and skills catalog API - Manage technical skills, tools, frameworks, and techniques")
public class KnowledgeController {
    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @Operation(summary = "Create new knowledge item", description = "Adds a new technical skill, tool, framework, or technique to the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Knowledge item created successfully",
                    content = @Content(schema = @Schema(implementation = KnowledgeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<KnowledgeDTO> createKnowledge(@RequestBody CreateKnowledgeCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(knowledgeService.createKnowledge(command));
    }

    @Operation(summary = "Get knowledge by ID", description = "Retrieves a specific knowledge item by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Knowledge item found",
                    content = @Content(schema = @Schema(implementation = KnowledgeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Knowledge item not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeDTO> getKnowledgeById(
            @Parameter(description = "Knowledge ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeById(id));
    }

    @Operation(summary = "Get all knowledge items", description = "Retrieves all knowledge items with optional filtering by category, approval status, or search term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Knowledge items retrieved successfully",
                    content = @Content(schema = @Schema(implementation = KnowledgeDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<KnowledgeDTO>> getAllKnowledge(
            @Parameter(description = "Filter by category ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Filter by approval status (Pending/Approved/Rejected)") @RequestParam(required = false) String status,
            @Parameter(description = "Search by knowledge name") @RequestParam(required = false) String search) {

        if (categoryId != null) {
            return ResponseEntity.ok(knowledgeService.getKnowledgeByCategoryId(categoryId));
        } else if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(knowledgeService.getKnowledgeByApprovalStatus(status));
        } else if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(knowledgeService.searchKnowledgeByName(search));
        } else {
            return ResponseEntity.ok(knowledgeService.getAllKnowledge());
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<KnowledgeDTO> approveKnowledge(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.approveKnowledge(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<KnowledgeDTO> rejectKnowledge(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.rejectKnowledge(id));
    }

    @PutMapping("/{id}/resubmit")
    public ResponseEntity<KnowledgeDTO> resubmitKnowledge(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.resubmitKnowledge(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Long id) {
        knowledgeService.deleteKnowledge(id);
        return ResponseEntity.noContent().build();
    }
}
