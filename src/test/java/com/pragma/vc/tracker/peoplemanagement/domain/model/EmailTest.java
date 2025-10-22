package com.pragma.vc.tracker.peoplemanagement.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Email value object
 */
class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        // Given
        String emailString = "juan.perez@pragma.com.co";

        // When
        Email email = Email.of(emailString);

        // Then
        assertNotNull(email);
        assertEquals("juan.perez@pragma.com.co", email.getValue());
    }

    @Test
    void shouldNormalizeEmailToLowerCase() {
        // Given
        String emailString = "Juan.Perez@PRAGMA.COM.CO";

        // When
        Email email = Email.of(emailString);

        // Then
        assertEquals("juan.perez@pragma.com.co", email.getValue());
    }

    @Test
    void shouldExtractDomain() {
        // Given
        Email email = Email.of("juan.perez@pragma.com.co");

        // When
        String domain = email.getDomain();

        // Then
        assertEquals("pragma.com.co", domain);
    }

    @Test
    void shouldExtractLocalPart() {
        // Given
        Email email = Email.of("juan.perez@pragma.com.co");

        // When
        String localPart = email.getLocalPart();

        // Then
        assertEquals("juan.perez", localPart);
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Email.of(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Email.of("   ")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> Email.of("invalid-email"));
        assertThrows(IllegalArgumentException.class, () -> Email.of("@pragma.com"));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test@"));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test @pragma.com"));
    }

    @Test
    void shouldBeEqualWhenEmailsAreTheSame() {
        // Given
        Email email1 = Email.of("test@pragma.com.co");
        Email email2 = Email.of("TEST@pragma.com.co"); // Case insensitive

        // Then
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void shouldAcceptVariousValidEmailFormats() {
        // These should all be valid
        assertDoesNotThrow(() -> Email.of("simple@example.com"));
        assertDoesNotThrow(() -> Email.of("user.name@example.com"));
        assertDoesNotThrow(() -> Email.of("user+tag@example.co.uk"));
        assertDoesNotThrow(() -> Email.of("user_name@example.org"));
        assertDoesNotThrow(() -> Email.of("123@example.com"));
    }
}