package com.pragma.vc.tracker.knowledgecatalog.infrastructure.web;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateKnowledgeCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.KnowledgeDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.KnowledgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @PostMapping
    public ResponseEntity<KnowledgeDTO> createKnowledge(@RequestBody CreateKnowledgeCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(knowledgeService.createKnowledge(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeDTO> getKnowledgeById(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeById(id));
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeDTO>> getAllKnowledge(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        
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
