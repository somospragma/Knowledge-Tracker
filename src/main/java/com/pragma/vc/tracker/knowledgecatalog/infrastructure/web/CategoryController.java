package com.pragma.vc.tracker.knowledgecatalog.infrastructure.web;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CategoryDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateCategoryCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.usecase.CategoryService;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "Knowledge Categories", description = "Knowledge category management API - Manage categories for classifying technical knowledge")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a new knowledge category", description = "Adds a new category for classifying knowledge items (e.g., Programming Language, Framework, Tool)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CreateCategoryCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(command));
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a specific knowledge category by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Get all categories", description = "Retrieves all knowledge categories in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Update category name", description = "Updates the name of an existing knowledge category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category name updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PutMapping("/{id}/name")
    public ResponseEntity<CategoryDTO> updateCategoryName(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return ResponseEntity.ok(categoryService.updateCategoryName(id, request.getName()));
    }

    @Operation(summary = "Delete category", description = "Deletes a knowledge category from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    public static class UpdateNameRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
