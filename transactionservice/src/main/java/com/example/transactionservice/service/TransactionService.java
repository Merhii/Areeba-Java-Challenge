package com.example.transactionservice.service;

import com.example.transactionservice.client.AccountClient;
import com.example.transactionservice.client.CardClient;
import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardClient cardClient;
    private final AccountClient accountClient;

    public TransactionService(TransactionRepository transactionRepository,
                              CardClient cardClient,
                              AccountClient accountClient) {
        this.transactionRepository = transactionRepository;
        this.cardClient = cardClient;
        this.accountClient = accountClient;
    }
    public List<Transaction> getTransactionsByCardId(UUID cardId) {
        return transactionRepository.findByCardId(cardId);
    }
    public Transaction createTransaction(UUID accountId, UUID cardId, BigDecimal amount, String type) {
        // 1. Validate card
        var card = cardClient.getCardById(cardId);
        if (!"ACTIVE".equals(card.status())) {
            throw new RuntimeException("Card is not active");
        }
        if (card.expiry().isBefore(LocalDate.now())) {
            throw new RuntimeException("Card is expired");
        }

        // 2. Validate account
        var account = accountClient.getAccountById(accountId);
        if (!"ACTIVE".equals(account.status())) {
            throw new RuntimeException("Account is not active");
        }

        // 3. Balance check + compute new balance
        BigDecimal newBalance = account.balance();
        if ("D".equalsIgnoreCase(type)) {
            if (account.balance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance");
            }
            newBalance = account.balance().subtract(amount);
        } else if ("C".equalsIgnoreCase(type)) {
            newBalance = account.balance().add(amount);
        } else {
            throw new RuntimeException("Invalid transaction type (must be C or D)");
        }

        // 4. Update account balance in account-service
        accountClient.updateBalance(accountId, newBalance);

        // 5. Save transaction record in DB
        Transaction transaction = Transaction.builder()

                .transactionAmount(amount)
                .transactionDate(Timestamp.from(Instant.now()))
                .transactionType(type.toUpperCase())
                .accountId(accountId)
                .cardId(cardId)
                .build();

        return transactionRepository.save(transaction);
    }
}
