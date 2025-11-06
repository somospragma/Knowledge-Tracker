package com.pragma.vc.tracker.peoplemanagement.infrastructure.web;

import com.pragma.vc.tracker.peoplemanagement.application.dto.ChapterDTO;
import com.pragma.vc.tracker.peoplemanagement.application.dto.CreateChapterCommand;
import com.pragma.vc.tracker.peoplemanagement.application.usecase.ChapterService;
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
 * REST Controller for Chapter operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/v1/chapters")
@Tag(name = "Chapters", description = "Organizational chapter management API - Manage organizational units within KC-Teams")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @Operation(summary = "Create a new chapter", description = "Adds a new organizational chapter within a KC-Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chapter created successfully",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ChapterDTO> createChapter(@RequestBody CreateChapterCommand command) {
        ChapterDTO chapter = chapterService.createChapter(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(chapter);
    }

    @Operation(summary = "Get chapter by ID", description = "Retrieves a specific chapter by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapter found",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chapter not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTO> getChapterById(
            @Parameter(description = "Chapter ID", required = true) @PathVariable Long id) {
        ChapterDTO chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapter);
    }

    @Operation(summary = "Get all chapters", description = "Retrieves all organizational chapters in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapters retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ChapterDTO>> getAllChapters() {
        List<ChapterDTO> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }

    @Operation(summary = "Update chapter name", description = "Updates the name of an existing chapter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapter name updated successfully",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chapter not found", content = @Content)
    })
    @PutMapping("/{id}/name")
    public ResponseEntity<ChapterDTO> updateChapterName(
            @Parameter(description = "Chapter ID", required = true) @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        ChapterDTO chapter = chapterService.updateChapterName(id, request.getName());
        return ResponseEntity.ok(chapter);
    }

    @Operation(summary = "Delete chapter", description = "Deletes a chapter from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Chapter deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Chapter not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(
            @Parameter(description = "Chapter ID", required = true) @PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

    // Inner class for request body
    public static class UpdateNameRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
