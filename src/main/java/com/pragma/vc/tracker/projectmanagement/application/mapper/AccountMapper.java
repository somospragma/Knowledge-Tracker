package com.pragma.vc.tracker.projectmanagement.application.mapper;

import com.pragma.vc.tracker.projectmanagement.application.dto.AccountDTO;
import com.pragma.vc.tracker.projectmanagement.domain.model.Account;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountStatus;

/**
 * Mapper to convert between Account domain model and DTOs
 */
public class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountDTO(
            account.getId() != null ? account.getId().getValue() : null,
            account.getName(),
            account.getTerritory(),
            account.getStatus().name(),
            account.getAttributes()
        );
    }

    public static AccountStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return AccountStatus.ACTIVE;
        }
        try {
            return AccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account status: " + status);
        }
    }
}