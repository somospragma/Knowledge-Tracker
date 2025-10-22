package com.pragma.vc.tracker.knowledgecatalog.infrastructure.web;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateLevelCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.LevelDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.LevelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
public class LevelController {
    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @PostMapping
    public ResponseEntity<LevelDTO> createLevel(@RequestBody CreateLevelCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(levelService.createLevel(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevelDTO> getLevelById(@PathVariable Long id) {
        return ResponseEntity.ok(levelService.getLevelById(id));
    }

    @GetMapping
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<LevelDTO> updateLevelName(@PathVariable Long id, @RequestBody UpdateNameRequest request) {
        return ResponseEntity.ok(levelService.updateLevelName(id, request.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        levelService.deleteLevel(id);
        return ResponseEntity.noContent().build();
    }

    public static class UpdateNameRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
