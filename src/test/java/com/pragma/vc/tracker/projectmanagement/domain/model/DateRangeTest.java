package com.pragma.vc.tracker.projectmanagement.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DateRange value object
 */
class DateRangeTest {

    @Test
    void shouldCreateDateRangeWithStartAndEndDate() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When
        DateRange dateRange = DateRange.of(start, end);

        // Then
        assertNotNull(dateRange);
        assertEquals(start, dateRange.getStartDate());
        assertEquals(end, dateRange.getEndDate());
        assertFalse(dateRange.isOngoing());
    }

    @Test
    void shouldCreateOngoingDateRange() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);

        // When
        DateRange dateRange = DateRange.from(start);

        // Then
        assertNotNull(dateRange);
        assertEquals(start, dateRange.getStartDate());
        assertNull(dateRange.getEndDate());
        assertTrue(dateRange.isOngoing());
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            DateRange.of(null, LocalDateTime.now())
        );
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 12, 31, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 0, 0);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            DateRange.of(start, end)
        );
    }

    @Test
    void shouldCalculateDurationInDays() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 11, 0, 0);
        DateRange dateRange = DateRange.of(start, end);

        // When
        long duration = dateRange.getDurationInDays();

        // Then
        assertEquals(10, duration);
    }

    @Test
    void shouldIdentifyCompletedDateRange() {
        // Given
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 12, 31, 0, 0);
        DateRange dateRange = DateRange.of(start, end);

        // Then
        assertTrue(dateRange.isCompleted());
        assertFalse(dateRange.isOngoing());
    }

    @Test
    void shouldBeEqualWhenDatesAreTheSame() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 31, 0, 0);
        DateRange dateRange1 = DateRange.of(start, end);
        DateRange dateRange2 = DateRange.of(start, end);

        // Then
        assertEquals(dateRange1, dateRange2);
        assertEquals(dateRange1.hashCode(), dateRange2.hashCode());
    }
}