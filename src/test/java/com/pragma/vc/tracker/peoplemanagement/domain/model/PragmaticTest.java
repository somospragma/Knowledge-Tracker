package com.pragma.vc.tracker.peoplemanagement.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Pragmatic domain entity
 * Tests business logic without any framework dependencies
 */
class PragmaticTest {

    @Test
    void shouldCreatePragmaticWithValidData() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("juan.perez@pragma.com.co");
        String firstName = "Juan";
        String lastName = "Pérez";
        PragmaticStatus status = PragmaticStatus.ACTIVE;

        // When
        Pragmatic pragmatic = Pragmatic.create(chapterId, email, firstName, lastName, status);

        // Then
        assertNotNull(pragmatic);
        assertEquals(chapterId, pragmatic.getChapterId());
        assertEquals(email, pragmatic.getEmail());
        assertEquals(firstName, pragmatic.getFirstName());
        assertEquals(lastName, pragmatic.getLastName());
        assertEquals(status, pragmatic.getStatus());
        assertEquals("Juan Pérez", pragmatic.getFullName());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);

        // When & Then
        // Email.of(null) throws IllegalArgumentException, which happens before Pragmatic is created
        assertThrows(IllegalArgumentException.class, () ->
            Pragmatic.create(chapterId, Email.of(null), "Juan", "Pérez", PragmaticStatus.ACTIVE)
        );
    }

    @Test
    void shouldThrowExceptionWhenFirstNameIsNull() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("test@pragma.com.co");

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Pragmatic.create(chapterId, email, null, "Pérez", PragmaticStatus.ACTIVE)
        );
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsNull() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("test@pragma.com.co");

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Pragmatic.create(chapterId, email, "Juan", null, PragmaticStatus.ACTIVE)
        );
    }

    @Test
    void shouldThrowExceptionWhenActivePragmaticHasNoChapter() {
        // Given
        Email email = Email.of("test@pragma.com.co");

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Pragmatic.create(null, email, "Juan", "Pérez", PragmaticStatus.ACTIVE)
        );
    }

    @Test
    void shouldAllowInactivePragmaticWithoutChapter() {
        // Given
        Email email = Email.of("test@pragma.com.co");

        // When
        Pragmatic pragmatic = Pragmatic.create(null, email, "Juan", "Pérez", PragmaticStatus.INACTIVE);

        // Then
        assertNotNull(pragmatic);
        assertNull(pragmatic.getChapterId());
    }

    @Test
    void shouldActivatePragmatic() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("test@pragma.com.co");
        Pragmatic pragmatic = Pragmatic.create(chapterId, email, "Juan", "Pérez", PragmaticStatus.INACTIVE);

        // When
        pragmatic.activate();

        // Then
        assertEquals(PragmaticStatus.ACTIVE, pragmatic.getStatus());
        assertTrue(pragmatic.isActive());
        assertTrue(pragmatic.canBeAssignedToProjects());
    }

    @Test
    void shouldThrowExceptionWhenActivatingWithoutChapter() {
        // Given
        Email email = Email.of("test@pragma.com.co");
        Pragmatic pragmatic = Pragmatic.create(null, email, "Juan", "Pérez", PragmaticStatus.INACTIVE);

        // When & Then
        assertThrows(IllegalStateException.class, pragmatic::activate);
    }

    @Test
    void shouldDeactivatePragmatic() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("test@pragma.com.co");
        Pragmatic pragmatic = Pragmatic.create(chapterId, email, "Juan", "Pérez", PragmaticStatus.ACTIVE);

        // When
        pragmatic.deactivate();

        // Then
        assertEquals(PragmaticStatus.INACTIVE, pragmatic.getStatus());
        assertFalse(pragmatic.isActive());
        assertFalse(pragmatic.canBeAssignedToProjects());
    }

    @Test
    void shouldPutPragmaticOnLeave() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("test@pragma.com.co");
        Pragmatic pragmatic = Pragmatic.create(chapterId, email, "Juan", "Pérez", PragmaticStatus.ACTIVE);

        // When
        pragmatic.putOnLeave();

        // Then
        assertEquals(PragmaticStatus.ON_LEAVE, pragmatic.getStatus());
        assertFalse(pragmatic.isActive());
    }

    @Test
    void shouldUpdateEmail() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email oldEmail = Email.of("old@pragma.com.co");
        Pragmatic pragmatic = Pragmatic.create(chapterId, oldEmail, "Juan", "Pérez", PragmaticStatus.ACTIVE);
        Email newEmail = Email.of("new@pragma.com.co");

        // When
        pragmatic.updateEmail(newEmail);

        // Then
        assertEquals(newEmail, pragmatic.getEmail());
    }

    @Test
    void shouldUpdateName() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("test@pragma.com.co");
        Pragmatic pragmatic = Pragmatic.create(chapterId, email, "Juan", "Pérez", PragmaticStatus.ACTIVE);

        // When
        pragmatic.updateName("Carlos", "Rodríguez");

        // Then
        assertEquals("Carlos", pragmatic.getFirstName());
        assertEquals("Rodríguez", pragmatic.getLastName());
        assertEquals("Carlos Rodríguez", pragmatic.getFullName());
    }

    @Test
    void shouldAssignToChapter() {
        // Given
        Email email = Email.of("test@pragma.com.co");
        Pragmatic pragmatic = Pragmatic.create(null, email, "Juan", "Pérez", PragmaticStatus.INACTIVE);
        ChapterId chapterId = ChapterId.of(1L);

        // When
        pragmatic.assignToChapter(chapterId);

        // Then
        assertEquals(chapterId, pragmatic.getChapterId());
    }

    @Test
    void shouldDefaultToActiveStatusWhenNotSpecified() {
        // Given
        ChapterId chapterId = ChapterId.of(1L);
        Email email = Email.of("test@pragma.com.co");

        // When
        Pragmatic pragmatic = Pragmatic.create(chapterId, email, "Juan", "Pérez", null);

        // Then
        assertEquals(PragmaticStatus.ACTIVE, pragmatic.getStatus());
    }
}