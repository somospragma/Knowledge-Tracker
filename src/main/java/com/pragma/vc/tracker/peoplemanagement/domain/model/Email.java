package com.pragma.vc.tracker.peoplemanagement.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a valid email address
 * Immutable and self-validating
 */
public class Email {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        String trimmed = value.trim();
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        this.value = trimmed.toLowerCase();
    }

    public static Email of(String value) {
        return new Email(value);
    }

    public String getValue() {
        return value;
    }

    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }

    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}