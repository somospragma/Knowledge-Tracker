package com.pragma.vc.tracker.projectmanagement.infrastructure.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.AccountDTO;
import com.pragma.vc.tracker.projectmanagement.application.dto.CreateAccountCommand;
import com.pragma.vc.tracker.projectmanagement.application.usecase.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Account operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody CreateAccountCommand command) {
        AccountDTO account = accountService.createAccount(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String region) {

        List<AccountDTO> accounts;

        if (status != null && !status.isBlank()) {
            accounts = accountService.getAccountsByStatus(status);
        } else if (region != null && !region.isBlank()) {
            accounts = accountService.getAccountsByRegion(region);
        } else {
            accounts = accountService.getAllAccounts();
        }

        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<AccountDTO> updateAccountName(
            @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        AccountDTO account = accountService.updateAccountName(id, request.getName());
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<AccountDTO> activateAccount(@PathVariable Long id) {
        AccountDTO account = accountService.activateAccount(id);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<AccountDTO> deactivateAccount(@PathVariable Long id) {
        AccountDTO account = accountService.deactivateAccount(id);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    // Inner class for request body
    public static class UpdateNameRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}