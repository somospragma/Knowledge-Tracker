package com.pragma.vc.tracker.projectmanagement.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DateRange value object
 */
class DateRangeTest {

    @Test
    void shouldCreateDateRangeWithStartAndEndDate() {
        // Given
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

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
        LocalDate start = LocalDate.of(2024, 1, 1);

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
            DateRange.of(null, LocalDate.now())
        );
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        // Given
        LocalDate start = LocalDate.of(2024, 12, 31);
        LocalDate end = LocalDate.of(2024, 1, 1);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            DateRange.of(start, end)
        );
    }

    @Test
    void shouldCalculateDurationInDays() {
        // Given
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 11);
        DateRange dateRange = DateRange.of(start, end);

        // When
        long duration = dateRange.getDurationInDays();

        // Then
        assertEquals(10, duration);
    }

    @Test
    void shouldIdentifyCompletedDateRange() {
        // Given
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 12, 31);
        DateRange dateRange = DateRange.of(start, end);

        // Then
        assertTrue(dateRange.isCompleted());
        assertFalse(dateRange.isOngoing());
    }

    @Test
    void shouldBeEqualWhenDatesAreTheSame() {
        // Given
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        DateRange dateRange1 = DateRange.of(start, end);
        DateRange dateRange2 = DateRange.of(start, end);

        // Then
        assertEquals(dateRange1, dateRange2);
        assertEquals(dateRange1.hashCode(), dateRange2.hashCode());
    }
}