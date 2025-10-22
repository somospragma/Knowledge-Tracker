package com.pragma.vc.tracker.knowledgeapplication.infrastructure.web;

import com.pragma.vc.tracker.knowledgeapplication.application.dto.AppliedKnowledgeDTO;
import com.pragma.vc.tracker.knowledgeapplication.application.dto.CreateAppliedKnowledgeCommand;
import com.pragma.vc.tracker.knowledgeapplication.application.dto.UpdateAppliedKnowledgeCommand;
import com.pragma.vc.tracker.knowledgeapplication.application.usecase.AppliedKnowledgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applied-knowledge")
public class AppliedKnowledgeController {
    private final AppliedKnowledgeService appliedKnowledgeService;

    public AppliedKnowledgeController(AppliedKnowledgeService appliedKnowledgeService) {
        this.appliedKnowledgeService = appliedKnowledgeService;
    }

    @PostMapping
    public ResponseEntity<AppliedKnowledgeDTO> createAppliedKnowledge(@RequestBody CreateAppliedKnowledgeCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appliedKnowledgeService.createAppliedKnowledge(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppliedKnowledgeDTO> getAppliedKnowledgeById(@PathVariable Long id) {
        return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeById(id));
    }

    @GetMapping
    public ResponseEntity<List<AppliedKnowledgeDTO>> getAppliedKnowledge(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long pragmaticId,
            @RequestParam(required = false) Long knowledgeId) {

        if (projectId != null && pragmaticId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByProjectAndPragmatic(projectId, pragmaticId));
        } else if (projectId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByProjectId(projectId));
        } else if (pragmaticId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByPragmaticId(pragmaticId));
        } else if (knowledgeId != null) {
            return ResponseEntity.ok(appliedKnowledgeService.getAppliedKnowledgeByKnowledgeId(knowledgeId));
        } else {
            return ResponseEntity.ok(appliedKnowledgeService.getAllAppliedKnowledge());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppliedKnowledgeDTO> updateAppliedKnowledge(
            @PathVariable Long id,
            @RequestBody UpdateAppliedKnowledgeCommand command) {
        return ResponseEntity.ok(appliedKnowledgeService.updateAppliedKnowledge(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppliedKnowledge(@PathVariable Long id) {
        appliedKnowledgeService.deleteAppliedKnowledge(id);
        return ResponseEntity.noContent().build();
    }
}