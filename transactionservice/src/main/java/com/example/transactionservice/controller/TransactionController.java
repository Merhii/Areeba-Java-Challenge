package com.example.transactionservice.controller;

import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @Operation(summary = "Get all transactions by card ID")
    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCard(@PathVariable UUID cardId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCardId(cardId));
    }
    @PostMapping
    public Transaction createTransaction(
            @RequestParam UUID accountId,
            @RequestParam UUID cardId,
            @RequestParam BigDecimal amount,
            @RequestParam String type) {
        return transactionService.createTransaction(accountId, cardId, amount, type);
    }

}
