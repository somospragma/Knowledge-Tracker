package com.pragma.vc.tracker.projectmanagement.infrastructure.adapter.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateRegionCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.RegionDTO;
import com.pragma.vc.tracker.projectmanagement.application.usecase.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Region operations
 * Handles HTTP requests and delegates to the application service
 */
@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * Create a new Region
     * POST /api/regions
     */
    @PostMapping
    public ResponseEntity<RegionDTO> createRegion(@RequestBody CreateRegionCommand command) {
        RegionDTO region = regionService.createRegion(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(region);
    }

    /**
     * Get all Regions
     * GET /api/regions
     */
    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        List<RegionDTO> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    /**
     * Get a Region by ID
     * GET /api/regions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable Long id) {
        RegionDTO region = regionService.getRegionById(id);
        return ResponseEntity.ok(region);
    }

    /**
     * Get a Region by name
     * GET /api/regions/by-name?name={name}
     */
    @GetMapping("/by-name")
    public ResponseEntity<RegionDTO> getRegionByName(@RequestParam String name) {
        RegionDTO region = regionService.getRegionByName(name);
        return ResponseEntity.ok(region);
    }

    /**
     * Update a Region
     * PUT /api/regions/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(
            @PathVariable Long id,
            @RequestBody CreateRegionCommand command) {
        RegionDTO region = regionService.updateRegion(id, command.getName());
        return ResponseEntity.ok(region);
    }

    /**
     * Delete a Region
     * DELETE /api/regions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
