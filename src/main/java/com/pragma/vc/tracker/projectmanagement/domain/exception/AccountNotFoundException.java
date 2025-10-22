package com.pragma.vc.tracker.projectmanagement.domain.exception;

import com.pragma.vc.tracker.projectmanagement.domain.model.AccountId;

/**
 * Domain exception thrown when an Account is not found
 */
public class AccountNotFoundException extends RuntimeException {
    private final AccountId accountId;

    public AccountNotFoundException(AccountId accountId) {
        super("Account not found with id: " + accountId);
        this.accountId = accountId;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}