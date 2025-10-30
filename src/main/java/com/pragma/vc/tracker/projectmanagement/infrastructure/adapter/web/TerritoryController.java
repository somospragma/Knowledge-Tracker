package com.pragma.vc.tracker.projectmanagement.infrastructure.adapter.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateTerritoryCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.TerritoryDTO;
import com.pragma.vc.tracker.projectmanagement.application.usecase.TerritoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Territory operations
 * Handles HTTP requests and delegates to the application service
 */
@RestController
@RequestMapping("/api/territories")
public class TerritoryController {

    private final TerritoryService territoryService;

    public TerritoryController(TerritoryService territoryService) {
        this.territoryService = territoryService;
    }

    /**
     * Create a new Territory
     * POST /api/territories
     */
    @PostMapping
    public ResponseEntity<TerritoryDTO> createTerritory(@RequestBody CreateTerritoryCommand command) {
        TerritoryDTO territory = territoryService.createTerritory(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(territory);
    }

    /**
     * Get all Territories
     * GET /api/territories
     */
    @GetMapping
    public ResponseEntity<List<TerritoryDTO>> getAllTerritories() {
        List<TerritoryDTO> territories = territoryService.getAllTerritories();
        return ResponseEntity.ok(territories);
    }

    /**
     * Get a Territory by ID
     * GET /api/territories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TerritoryDTO> getTerritoryById(@PathVariable Long id) {
        TerritoryDTO territory = territoryService.getTerritoryById(id);
        return ResponseEntity.ok(territory);
    }

    /**
     * Get a Territory by name
     * GET /api/territories/by-name?name={name}
     */
    @GetMapping("/by-name")
    public ResponseEntity<TerritoryDTO> getTerritoryByName(@RequestParam String name) {
        TerritoryDTO territory = territoryService.getTerritoryByName(name);
        return ResponseEntity.ok(territory);
    }

    /**
     * Update a Territory
     * PUT /api/territories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TerritoryDTO> updateTerritory(
            @PathVariable Long id,
            @RequestBody CreateTerritoryCommand command) {
        TerritoryDTO territory = territoryService.updateTerritory(id, command.getName());
        return ResponseEntity.ok(territory);
    }

    /**
     * Delete a Territory
     * DELETE /api/territories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerritory(@PathVariable Long id) {
        territoryService.deleteTerritory(id);
        return ResponseEntity.noContent().build();
    }
}
