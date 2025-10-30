package com.pragma.vc.tracker.projectmanagement.domain.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Account domain entity
 * Tests business logic without any framework dependencies
 */
class AccountTest {

    @Test
    void shouldCreateAccountWithValidData() {
        // Given
        String name = "Bancolombia";
        String territory = "Latin America";
        AccountStatus status = AccountStatus.ACTIVE;
        Map<String, String> attributes = new HashMap<>();
        attributes.put("industry", "Banking");

        // When
        Account account = Account.create(name, territory, status, attributes);

        // Then
        assertNotNull(account);
        assertEquals(name, account.getName());
        assertEquals(territory, account.getTerritory());
        assertEquals(status, account.getStatus());
        assertEquals("Banking", account.getAttribute("industry"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Given
        String name = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Account.create(name, "Territory", AccountStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        // Given
        String name = "   ";

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Account.create(name, "Territory", AccountStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameExceeds255Characters() {
        // Given
        String name = "A".repeat(256);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Account.create(name, "Territory", AccountStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldActivateInactiveAccount() {
        // Given
        Account account = Account.create("Test Account", "Territory", AccountStatus.INACTIVE, null);

        // When
        account.activate();

        // Then
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertTrue(account.isActive());
    }

    @Test
    void shouldThrowExceptionWhenActivatingAlreadyActiveAccount() {
        // Given
        Account account = Account.create("Test Account", "Territory", AccountStatus.ACTIVE, null);

        // When & Then
        assertThrows(IllegalStateException.class, account::activate);
    }

    @Test
    void shouldDeactivateActiveAccount() {
        // Given
        Account account = Account.create("Test Account", "Territory", AccountStatus.ACTIVE, null);

        // When
        account.deactivate();

        // Then
        assertEquals(AccountStatus.INACTIVE, account.getStatus());
        assertFalse(account.isActive());
    }

    @Test
    void shouldUpdateAccountName() {
        // Given
        Account account = Account.create("Old Name", "Territory", AccountStatus.ACTIVE, null);
        String newName = "New Name";

        // When
        account.updateName(newName);

        // Then
        assertEquals(newName, account.getName());
    }

    @Test
    void shouldAddAndRemoveAttributes() {
        // Given
        Account account = Account.create("Test Account", "Territory", AccountStatus.ACTIVE, null);

        // When
        account.addAttribute("key1", "value1");
        account.addAttribute("key2", "value2");

        // Then
        assertEquals("value1", account.getAttribute("key1"));
        assertEquals("value2", account.getAttribute("key2"));

        // When
        account.removeAttribute("key1");

        // Then
        assertNull(account.getAttribute("key1"));
        assertEquals("value2", account.getAttribute("key2"));
    }

    @Test
    void shouldDefaultToActiveStatusWhenNotSpecified() {
        // When
        Account account = Account.create("Test Account", "Territory", null, null);

        // Then
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void shouldPreventIdChangeOnceSet() {
        // Given
        Account account = Account.create("Test Account", "Territory", AccountStatus.ACTIVE, null);
        AccountId id1 = AccountId.of(1L);
        AccountId id2 = AccountId.of(2L);

        // When
        account.setId(id1);

        // Then
        assertThrows(IllegalStateException.class, () -> account.setId(id2));
    }
}