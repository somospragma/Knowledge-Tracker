package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateCategoryWithValidName() {
        Category category = Category.create("Platform");
        
        assertNotNull(category);
        assertEquals("Platform", category.getName());
        assertNull(category.getId());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Category.create(null));
    }

    @Test
    void shouldUpdateCategoryName() {
        Category category = Category.create("Old Name");
        category.updateName("New Name");
        
        assertEquals("New Name", category.getName());
    }

    @Test
    void shouldPreventIdChangeOnceSet() {
        Category category = Category.create("Test");
        CategoryId id1 = CategoryId.of(1L);
        category.setId(id1);
        
        assertThrows(IllegalStateException.class, () -> category.setId(CategoryId.of(2L)));
    }
}
