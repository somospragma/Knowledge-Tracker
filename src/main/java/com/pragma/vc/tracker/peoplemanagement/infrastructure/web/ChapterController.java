package com.pragma.vc.tracker.peoplemanagement.infrastructure.web;

import com.pragma.vc.tracker.peoplemanagement.application.dto.ChapterDTO;
import com.pragma.vc.tracker.peoplemanagement.application.dto.CreateChapterCommand;
import com.pragma.vc.tracker.peoplemanagement.application.usecase.ChapterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Chapter operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping
    public ResponseEntity<ChapterDTO> createChapter(@RequestBody CreateChapterCommand command) {
        ChapterDTO chapter = chapterService.createChapter(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(chapter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTO> getChapterById(@PathVariable Long id) {
        ChapterDTO chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapter);
    }

    @GetMapping
    public ResponseEntity<List<ChapterDTO>> getAllChapters() {
        List<ChapterDTO> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<ChapterDTO> updateChapterName(
            @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        ChapterDTO chapter = chapterService.updateChapterName(id, request.getName());
        return ResponseEntity.ok(chapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
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