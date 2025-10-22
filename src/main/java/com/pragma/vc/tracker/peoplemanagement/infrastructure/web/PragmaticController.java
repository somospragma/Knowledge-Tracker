package com.pragma.vc.tracker.peoplemanagement.infrastructure.web;

import com.pragma.vc.tracker.peoplemanagement.application.dto.CreatePragmaticCommand;
import com.pragma.vc.tracker.peoplemanagement.application.dto.PragmaticDTO;
import com.pragma.vc.tracker.peoplemanagement.application.usecase.PragmaticService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Pragmatic operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/pragmatics")
public class PragmaticController {

    private final PragmaticService pragmaticService;

    public PragmaticController(PragmaticService pragmaticService) {
        this.pragmaticService = pragmaticService;
    }

    @PostMapping
    public ResponseEntity<PragmaticDTO> createPragmatic(@RequestBody CreatePragmaticCommand command) {
        PragmaticDTO pragmatic = pragmaticService.createPragmatic(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(pragmatic);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PragmaticDTO> getPragmaticById(@PathVariable Long id) {
        PragmaticDTO pragmatic = pragmaticService.getPragmaticById(id);
        return ResponseEntity.ok(pragmatic);
    }

    @GetMapping
    public ResponseEntity<List<PragmaticDTO>> getAllPragmatics(
            @RequestParam(required = false) Long chapterId,
            @RequestParam(required = false) String status) {

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

    @PutMapping("/{id}/email")
    public ResponseEntity<PragmaticDTO> updatePragmaticEmail(
            @PathVariable Long id,
            @RequestBody UpdateEmailRequest request) {
        PragmaticDTO pragmatic = pragmaticService.updatePragmaticEmail(id, request.getEmail());
        return ResponseEntity.ok(pragmatic);
    }

    @PutMapping("/{id}/chapter")
    public ResponseEntity<PragmaticDTO> assignToChapter(
            @PathVariable Long id,
            @RequestBody AssignChapterRequest request) {
        PragmaticDTO pragmatic = pragmaticService.assignToChapter(id, request.getChapterId());
        return ResponseEntity.ok(pragmatic);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<PragmaticDTO> activatePragmatic(@PathVariable Long id) {
        PragmaticDTO pragmatic = pragmaticService.activatePragmatic(id);
        return ResponseEntity.ok(pragmatic);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<PragmaticDTO> deactivatePragmatic(@PathVariable Long id) {
        PragmaticDTO pragmatic = pragmaticService.deactivatePragmatic(id);
        return ResponseEntity.ok(pragmatic);
    }

    @PutMapping("/{id}/on-leave")
    public ResponseEntity<PragmaticDTO> putPragmaticOnLeave(@PathVariable Long id) {
        PragmaticDTO pragmatic = pragmaticService.putPragmaticOnLeave(id);
        return ResponseEntity.ok(pragmatic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePragmatic(@PathVariable Long id) {
        pragmaticService.deletePragmatic(id);
        return ResponseEntity.noContent().build();
    }

    // Inner classes for request bodies
    public static class UpdateEmailRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class AssignChapterRequest {
        private Long chapterId;

        public Long getChapterId() {
            return chapterId;
        }

        public void setChapterId(Long chapterId) {
            this.chapterId = chapterId;
        }
    }
}