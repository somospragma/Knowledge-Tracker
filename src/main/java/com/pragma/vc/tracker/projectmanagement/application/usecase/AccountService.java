package com.pragma.vc.tracker.projectmanagement.application.usecase;

import com.pragma.vc.tracker.projectmanagement.application.dto.AccountDTO;
import com.pragma.vc.tracker.projectmanagement.application.dto.CreateAccountCommand;
import com.pragma.vc.tracker.projectmanagement.application.mapper.AccountMapper;
import com.pragma.vc.tracker.projectmanagement.domain.exception.AccountNotFoundException;
import com.pragma.vc.tracker.projectmanagement.domain.exception.DuplicateAccountNameException;
import com.pragma.vc.tracker.projectmanagement.domain.model.Account;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountId;
import com.pragma.vc.tracker.projectmanagement.domain.model.AccountStatus;
import com.pragma.vc.tracker.projectmanagement.domain.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service for Account use cases
 * Orchestrates domain operations and coordinates with repositories
 */
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new Account
     */
    public AccountDTO createAccount(CreateAccountCommand command) {
        // Check for duplicate name
        if (accountRepository.existsByName(command.getName())) {
            throw new DuplicateAccountNameException(command.getName());
        }

        // Create domain entity
        AccountStatus status = AccountMapper.parseStatus(command.getStatus());
        Account account = Account.create(
            command.getName(),
            command.getTerritory(),
            status,
            command.getAttributes()
        );

        // Save and return
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.toDTO(savedAccount);
    }

    /**
     * Get an Account by ID
     */
    public AccountDTO getAccountById(Long id) {
        AccountId accountId = AccountId.of(id);
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
        return AccountMapper.toDTO(account);
    }

    /**
     * Get all Accounts
     */
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(AccountMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get Accounts by status
     */
    public List<AccountDTO> getAccountsByStatus(String status) {
        AccountStatus accountStatus = AccountMapper.parseStatus(status);
        return accountRepository.findByStatus(accountStatus).stream()
            .map(AccountMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get Accounts by region
     */
    public List<AccountDTO> getAccountsByTerritory(String territory) {
        return accountRepository.findByTerritory(territory).stream()
            .map(AccountMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update Account name
     */
    public AccountDTO updateAccountName(Long id, String newName) {
        AccountId accountId = AccountId.of(id);
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Check if new name is already taken by another account
        accountRepository.findByName(newName).ifPresent(existing -> {
            if (!existing.getId().equals(accountId)) {
                throw new DuplicateAccountNameException(newName);
            }
        });

        account.updateName(newName);
        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.toDTO(updatedAccount);
    }

    /**
     * Activate an Account
     */
    public AccountDTO activateAccount(Long id) {
        AccountId accountId = AccountId.of(id);
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.activate();
        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.toDTO(updatedAccount);
    }

    /**
     * Deactivate an Account
     */
    public AccountDTO deactivateAccount(Long id) {
        AccountId accountId = AccountId.of(id);
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.deactivate();
        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.toDTO(updatedAccount);
    }

    /**
     * Delete an Account
     */
    public void deleteAccount(Long id) {
        AccountId accountId = AccountId.of(id);
        if (!accountRepository.findById(accountId).isPresent()) {
            throw new AccountNotFoundException(accountId);
        }
        accountRepository.deleteById(accountId);
    }
}