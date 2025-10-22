package com.pragma.vc.tracker.knowledgecatalog.application.usecase;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CategoryDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateCategoryCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.mapper.CategoryMapper;
import com.pragma.vc.tracker.knowledgecatalog.domain.exception.CategoryNotFoundException;
import com.pragma.vc.tracker.knowledgecatalog.domain.exception.DuplicateCategoryNameException;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.Category;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.CategoryId;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO createCategory(CreateCategoryCommand command) {
        if (categoryRepository.existsByName(command.getName())) {
            throw new DuplicateCategoryNameException(command.getName());
        }
        Category category = Category.create(command.getName());
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDTO(saved);
    }

    public CategoryDTO getCategoryById(Long id) {
        CategoryId categoryId = CategoryId.of(id);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return CategoryMapper.toDTO(category);
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    public CategoryDTO updateCategoryName(Long id, String newName) {
        CategoryId categoryId = CategoryId.of(id);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        categoryRepository.findByName(newName).ifPresent(existing -> {
            if (!existing.getId().equals(categoryId)) {
                throw new DuplicateCategoryNameException(newName);
            }
        });

        category.updateName(newName);
        Category updated = categoryRepository.save(category);
        return CategoryMapper.toDTO(updated);
    }

    public void deleteCategory(Long id) {
        CategoryId categoryId = CategoryId.of(id);
        if (!categoryRepository.findById(categoryId).isPresent()) {
            throw new CategoryNotFoundException(categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }
}
