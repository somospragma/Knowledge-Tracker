package com.pragma.vc.tracker.knowledgecatalog.infrastructure.web;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateLevelCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.LevelDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.LevelService;
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
@RequestMapping("/api/v1/levels")
@Tag(name = "Proficiency Levels", description = "Knowledge proficiency level management API - Manage proficiency levels (Beginner, Intermediate, Advanced, Expert)")
public class LevelController {
    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @Operation(summary = "Create a new proficiency level", description = "Adds a new knowledge proficiency level to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Level created successfully",
                    content = @Content(schema = @Schema(implementation = LevelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<LevelDTO> createLevel(@RequestBody CreateLevelCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(levelService.createLevel(command));
    }

    @Operation(summary = "Get level by ID", description = "Retrieves a specific proficiency level by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Level found",
                    content = @Content(schema = @Schema(implementation = LevelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Level not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<LevelDTO> getLevelById(
            @Parameter(description = "Level ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(levelService.getLevelById(id));
    }

    @Operation(summary = "Get all proficiency levels", description = "Retrieves all knowledge proficiency levels in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Levels retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LevelDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }

    @Operation(summary = "Update level name", description = "Updates the name of an existing proficiency level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Level name updated successfully",
                    content = @Content(schema = @Schema(implementation = LevelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Level not found", content = @Content)
    })
    @PutMapping("/{id}/name")
    public ResponseEntity<LevelDTO> updateLevelName(
            @Parameter(description = "Level ID", required = true) @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return ResponseEntity.ok(levelService.updateLevelName(id, request.getName()));
    }

    @Operation(summary = "Delete proficiency level", description = "Deletes a proficiency level from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Level deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Level not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(
            @Parameter(description = "Level ID", required = true) @PathVariable Long id) {
        levelService.deleteLevel(id);
        return ResponseEntity.noContent().build();
    }

    public static class UpdateNameRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
