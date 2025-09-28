package com.example.transactionservice.service;

import com.example.transactionservice.client.AccountClient;
import com.example.transactionservice.client.CardClient;
import com.example.transactionservice.client.FraudClient;
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
    private final FraudClient fraudClient;
    public TransactionService(TransactionRepository transactionRepository,
                              CardClient cardClient,
                              AccountClient accountClient,
                              FraudClient fraudClient) {
        this.transactionRepository = transactionRepository;
        this.cardClient = cardClient;
        this.accountClient = accountClient;
        this.fraudClient = fraudClient;
    }
    public List<Transaction> getTransactionsByCardId(UUID cardId) {
        return transactionRepository.findByCardId(cardId);
    }

    public Transaction createTransaction(UUID accountId, UUID cardId, BigDecimal amount, String type) {

        var card = cardClient.getCardById(cardId);
        if (!"ACTIVE".equals(card.status())) {
            throw new RuntimeException("Card is not active");
        }
        if (card.expiry().isBefore(LocalDate.now())) {
            throw new RuntimeException("Card is expired");
        }

        var account = accountClient.getAccountById(accountId);
        if (!"ACTIVE".equals(account.status())) {
            throw new RuntimeException("Account is not active");
        }

        // Fraud detection step
        boolean fraudulent = fraudClient.checkFraud(cardId, amount);
        if (fraudulent) {
            Transaction rejectedTx = Transaction.builder()

                    .transactionAmount(amount)
                    .transactionDate(Timestamp.from(Instant.now()))
                    .transactionType(type)
                    .accountId(accountId)
                    .cardId(cardId)
                    .response("REJECTED:")
                    .build();

            return transactionRepository.save(rejectedTx);
        }

        // Normal balance update
        BigDecimal newBalance = account.balance();
        if ("D".equals(type)) {
            if (account.balance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance");
            }
            newBalance = account.balance().subtract(amount);
        } else if ("C".equals(type)) {
            newBalance = account.balance().add(amount);
        } else {
            throw new RuntimeException("Invalid transaction type");
        }

        accountClient.updateBalance(accountId, newBalance);

        Transaction transaction = Transaction.builder()

                .transactionAmount(amount)
                .transactionDate(Timestamp.from(Instant.now()))
                .transactionType(type)
                .accountId(accountId)
                .cardId(cardId)
                .response("APPROVED")
                .build();

        return transactionRepository.save(transaction);
    }
}
