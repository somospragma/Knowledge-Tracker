package com.pragma.vc.tracker.peoplemanagement.infrastructure.web;

import com.pragma.vc.tracker.peoplemanagement.application.dto.CreatePragmaticCommand;
import com.pragma.vc.tracker.peoplemanagement.application.dto.PragmaticDTO;
import com.pragma.vc.tracker.peoplemanagement.application.usecase.PragmaticService;
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

/**
 * REST Controller for Pragmatic operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/v1/pragmatics")
@Tag(name = "Pragmatics", description = "Employee management API - Manage Pragma SA employees (Pragmatics)")
public class PragmaticController {

    private final PragmaticService pragmaticService;

    public PragmaticController(PragmaticService pragmaticService) {
        this.pragmaticService = pragmaticService;
    }

    @Operation(summary = "Create a new pragmatic", description = "Adds a new Pragma SA employee to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pragmatic created successfully",
                    content = @Content(schema = @Schema(implementation = PragmaticDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PragmaticDTO> createPragmatic(@RequestBody CreatePragmaticCommand command) {
        PragmaticDTO pragmatic = pragmaticService.createPragmatic(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(pragmatic);
    }

    @Operation(summary = "Get pragmatic by ID", description = "Retrieves a specific employee by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pragmatic found",
                    content = @Content(schema = @Schema(implementation = PragmaticDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pragmatic not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PragmaticDTO> getPragmaticById(
            @Parameter(description = "Pragmatic ID", required = true) @PathVariable Long id) {
        PragmaticDTO pragmatic = pragmaticService.getPragmaticById(id);
        return ResponseEntity.ok(pragmatic);
    }

    @Operation(summary = "Get all pragmatics", description = "Retrieves all employees with optional filtering by chapter or status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pragmatics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PragmaticDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PragmaticDTO>> getAllPragmatics(
            @Parameter(description = "Filter by chapter ID") @RequestParam(required = false) Long chapterId,
            @Parameter(description = "Filter by status (Active/Inactive/OnLeave)") @RequestParam(required = false) String status) {

        List<PragmaticDTO> pragmatics;

        if (chapterId != null) {
            pragmatics = pragmaticService.getPragmaticsByChapterId(chapterId);
        } else if (status != null && !status.isBlank()) {
            pragmatics = pragmaticService.getPragmaticsByStatus(status);
        } else {
            pragmatics = pragmaticService.getAllPragmatics();
        }

        return ResponseEntity.ok(pragmatics);
    }

    @Operation(summary = "Update pragmatic email", description = "Updates an employee's email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email updated successfully",
                    content = @Content(schema = @Schema(implementation = PragmaticDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pragmatic not found", content = @Content)
    })
    @PutMapping("/{id}/email")
    public ResponseEntity<PragmaticDTO> updatePragmaticEmail(
            @Parameter(description = "Pragmatic ID", required = true) @PathVariable Long id,
            @RequestBody UpdateEmailRequest request) {
        PragmaticDTO pragmatic = pragmaticService.updatePragmaticEmail(id, request.getEmail());
        return ResponseEntity.ok(pragmatic);
    }

    @Operation(summary = "Activate pragmatic", description = "Activates an inactive or on-leave employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pragmatic activated successfully",
                    content = @Content(schema = @Schema(implementation = PragmaticDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pragmatic not found", content = @Content)
    })
    @PutMapping("/{id}/activate")
    public ResponseEntity<PragmaticDTO> activatePragmatic(
            @Parameter(description = "Pragmatic ID", required = true) @PathVariable Long id) {
        PragmaticDTO pragmatic = pragmaticService.activatePragmatic(id);
        return ResponseEntity.ok(pragmatic);
    }

    @Operation(summary = "Deactivate pragmatic", description = "Marks an employee as inactive")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pragmatic deactivated successfully",
                    content = @Content(schema = @Schema(implementation = PragmaticDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pragmatic not found", content = @Content)
    })
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<PragmaticDTO> deactivatePragmatic(
            @Parameter(description = "Pragmatic ID", required = true) @PathVariable Long id) {
        PragmaticDTO pragmatic = pragmaticService.deactivatePragmatic(id);
        return ResponseEntity.ok(pragmatic);
    }

    @Operation(summary = "Delete pragmatic", description = "Deletes an employee from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pragmatic deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pragmatic not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePragmatic(
            @Parameter(description = "Pragmatic ID", required = true) @PathVariable Long id) {
        pragmaticService.deletePragmatic(id);
        return ResponseEntity.noContent().build();
    }

    // Inner class for request body
    public static class UpdateEmailRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
