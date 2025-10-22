package com.pragma.vc.tracker.knowledgecatalog.infrastructure.web;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CategoryDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateCategoryCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CreateCategoryCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<CategoryDTO> updateCategoryName(@PathVariable Long id, @RequestBody UpdateNameRequest request) {
        return ResponseEntity.ok(categoryService.updateCategoryName(id, request.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    public static class UpdateNameRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
