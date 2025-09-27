package com.example.account_service.service;

import com.example.account_service.entity.Account;
import com.example.account_service.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(BigDecimal initialBalance) {
        Account account = Account.builder()
                .balance(initialBalance)
                .status("INACTIVE")
                .build();
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById(UUID id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account updateAccount(UUID id, BigDecimal newBalance) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }

    public Account activateAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("ACTIVE");
        return accountRepository.save(account);
    }

    public Account deactivateAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("INACTIVE");
        return accountRepository.save(account);
    }
}

