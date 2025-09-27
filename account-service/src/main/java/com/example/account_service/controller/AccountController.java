package com.example.account_service.controller;

import com.example.account_service.entity.Account;
import com.example.account_service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "CRUD operations for accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account")
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam BigDecimal initialBalance) {
        return ResponseEntity.ok(accountService.createAccount(initialBalance));
    }

    @Operation(summary = "Retrieve account by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable UUID id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all accounts")
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @Operation(summary = "Update account balance")
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable UUID id,
                                                 @RequestParam BigDecimal balance) {
        return ResponseEntity.ok(accountService.updateAccount(id, balance));
    }

    @Operation(summary = "Delete an account")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate an account")
    @PutMapping("/{id}/activate")
    public ResponseEntity<Account> activateAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.activateAccount(id));
    }

    @Operation(summary = "Deactivate an account")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Account> deactivateAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.deactivateAccount(id));
    }
}
