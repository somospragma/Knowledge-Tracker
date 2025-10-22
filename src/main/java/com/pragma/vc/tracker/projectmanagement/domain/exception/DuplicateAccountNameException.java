package com.pragma.vc.tracker.projectmanagement.domain.exception;

/**
 * Domain exception thrown when attempting to create an Account with a duplicate name
 */
public class DuplicateAccountNameException extends RuntimeException {
    private final String accountName;

    public DuplicateAccountNameException(String accountName) {
        super("Account already exists with name: " + accountName);
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }
}