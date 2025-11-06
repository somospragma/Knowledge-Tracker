package com.pragma.vc.tracker.projectmanagement.infrastructure.web;

import com.pragma.vc.tracker.projectmanagement.application.dto.AccountDTO;
import com.pragma.vc.tracker.projectmanagement.application.dto.CreateAccountCommand;
import com.pragma.vc.tracker.projectmanagement.application.usecase.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Account operations
 * Input adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "Client account management API - Manage client organizations and their projects")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new client account", description = "Creates a new client organization in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody CreateAccountCommand command) {
        AccountDTO account = accountService.createAccount(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @Operation(summary = "Get account by ID", description = "Retrieves a specific client account by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(
            @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Get all accounts", description = "Retrieves all client accounts with optional filtering by status or territory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(
            @Parameter(description = "Filter by account status (Active/Inactive)") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by territory name") @RequestParam(required = false) String territory) {

        List<AccountDTO> accounts;

        if (status != null && !status.isBlank()) {
            accounts = accountService.getAccountsByStatus(status);
        } else if (territory != null && !territory.isBlank()) {
            accounts = accountService.getAccountsByTerritory(territory);
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