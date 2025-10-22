package com.pragma.vc.tracker.projectmanagement.domain.model;

import java.util.Objects;

/**
 * Value Object representing Account identifier
 * Immutable and self-validating
 */
public class AccountId {
    private final Long value;

    private AccountId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Account ID must be a positive number");
        }
        this.value = value;
    }

    public static AccountId of(Long value) {
        return new AccountId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId = (AccountId) o;
        return Objects.equals(value, accountId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "AccountId{" + value + "}";
    }
}