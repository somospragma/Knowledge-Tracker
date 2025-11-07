package com.pragma.vc.tracker.projectmanagement.infrastructure.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateTerritoryCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.TerritoryDTO;
import com.pragma.vc.tracker.projectmanagement.application.usecase.TerritoryService;
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
 * REST Controller for Territory operations
 * Handles HTTP requests and delegates to the application service
 */
@RestController
@RequestMapping("/api/v1/territories")
@Tag(name = "Territories", description = "Geographic territory management API - Manage regions where Pragma SA operates")
public class TerritoryController {

    private final TerritoryService territoryService;

    public TerritoryController(TerritoryService territoryService) {
        this.territoryService = territoryService;
    }

    @Operation(summary = "Create a new territory", description = "Adds a new geographic territory where Pragma SA operates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Territory created successfully",
                    content = @Content(schema = @Schema(implementation = TerritoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TerritoryDTO> createTerritory(@RequestBody CreateTerritoryCommand command) {
        TerritoryDTO territory = territoryService.createTerritory(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(territory);
    }

    @Operation(summary = "Get all territories", description = "Retrieves all geographic territories in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Territories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TerritoryDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TerritoryDTO>> getAllTerritories() {
        List<TerritoryDTO> territories = territoryService.getAllTerritories();
        return ResponseEntity.ok(territories);
    }

    @Operation(summary = "Get territory by ID", description = "Retrieves a specific territory by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Territory found",
                    content = @Content(schema = @Schema(implementation = TerritoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Territory not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TerritoryDTO> getTerritoryById(
            @Parameter(description = "Territory ID", required = true) @PathVariable Long id) {
        TerritoryDTO territory = territoryService.getTerritoryById(id);
        return ResponseEntity.ok(territory);
    }

    @Operation(summary = "Get territory by name", description = "Retrieves a territory by its name (e.g., Colombia, México)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Territory found",
                    content = @Content(schema = @Schema(implementation = TerritoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Territory not found", content = @Content)
    })
    @GetMapping("/by-name")
    public ResponseEntity<TerritoryDTO> getTerritoryByName(
            @Parameter(description = "Territory name", required = true, example = "Colombia") @RequestParam String name) {
        TerritoryDTO territory = territoryService.getTerritoryByName(name);
        return ResponseEntity.ok(territory);
    }

    @Operation(summary = "Update territory", description = "Updates an existing territory's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Territory updated successfully",
                    content = @Content(schema = @Schema(implementation = TerritoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Territory not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TerritoryDTO> updateTerritory(
            @Parameter(description = "Territory ID", required = true) @PathVariable Long id,
            @RequestBody CreateTerritoryCommand command) {
        TerritoryDTO territory = territoryService.updateTerritory(id, command.getName());
        return ResponseEntity.ok(territory);
    }

    @Operation(summary = "Delete territory", description = "Deletes a territory from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Territory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Territory not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerritory(
            @Parameter(description = "Territory ID", required = true) @PathVariable Long id) {
        territoryService.deleteTerritory(id);
        return ResponseEntity.noContent().build();
    }
}
