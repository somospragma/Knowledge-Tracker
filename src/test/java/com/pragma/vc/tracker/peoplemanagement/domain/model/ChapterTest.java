package com.pragma.vc.tracker.peoplemanagement.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Chapter domain entity
 * Tests business logic without any framework dependencies
 */
class ChapterTest {

    @Test
    void shouldCreateChapterWithValidName() {
        // Given
        String name = "Backend Development";

        // When
        Chapter chapter = Chapter.create(name);

        // Then
        assertNotNull(chapter);
        assertEquals(name, chapter.getName());
        assertNull(chapter.getId());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Chapter.create(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Chapter.create("   ")
        );
    }

    @Test
    void shouldThrowExceptionWhenNameExceeds255Characters() {
        // Given
        String longName = "A".repeat(256);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Chapter.create(longName)
        );
    }

    @Test
    void shouldUpdateChapterName() {
        // Given
        Chapter chapter = Chapter.create("Old Name");
        String newName = "New Name";

        // When
        chapter.updateName(newName);

        // Then
        assertEquals(newName, chapter.getName());
    }

    @Test
    void shouldPreventIdChangeOnceSet() {
        // Given
        Chapter chapter = Chapter.create("Test Chapter");
        ChapterId id1 = ChapterId.of(1L);
        ChapterId id2 = ChapterId.of(2L);

        // When
        chapter.setId(id1);

        // Then
        assertEquals(id1, chapter.getId());
        assertThrows(IllegalStateException.class, () -> chapter.setId(id2));
    }

    @Test
    void shouldReconstituteChapterFromPersistence() {
        // Given
        ChapterId id = ChapterId.of(1L);
        String name = "DevOps";

        // When
        Chapter chapter = Chapter.reconstitute(id, name);

        // Then
        assertNotNull(chapter);
        assertEquals(id, chapter.getId());
        assertEquals(name, chapter.getName());
    }

    @Test
    void shouldBeEqualWhenIdsAreTheSame() {
        // Given
        ChapterId id = ChapterId.of(1L);
        Chapter chapter1 = Chapter.reconstitute(id, "Chapter 1");
        Chapter chapter2 = Chapter.reconstitute(id, "Chapter 2");

        // Then
        assertEquals(chapter1, chapter2);
        assertEquals(chapter1.hashCode(), chapter2.hashCode());
    }
}