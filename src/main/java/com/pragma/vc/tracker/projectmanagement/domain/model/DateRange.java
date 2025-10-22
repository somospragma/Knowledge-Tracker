package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Value Object representing a date range with start and end dates
 * Immutable and self-validating
 */
public class DateRange {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    private DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange of(LocalDateTime startDate, LocalDateTime endDate) {
        return new DateRange(startDate, endDate);
    }

    public static DateRange from(LocalDateTime startDate) {
        return new DateRange(startDate, null);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isOngoing() {
        return endDate == null;
    }

    public boolean isCompleted() {
        return endDate != null && endDate.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return !startDate.isAfter(now) && (endDate == null || endDate.isAfter(now));
    }

    public long getDurationInDays() {
        if (endDate == null) {
            return ChronoUnit.DAYS.between(startDate, LocalDateTime.now());
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(startDate, dateRange.startDate) &&
               Objects.equals(endDate, dateRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        return "DateRange{" +
               "startDate=" + startDate +
               ", endDate=" + (endDate != null ? endDate : "ongoing") +
               '}';
    }
}