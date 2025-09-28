package com.example.transactionservice;

import com.example.transactionservice.client.AccountClient;
import com.example.transactionservice.client.CardClient;
import com.example.transactionservice.client.FraudClient;
import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import com.example.transactionservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardClient cardClient;

    @Mock
    private AccountClient accountClient;

    @Mock
    private FraudClient fraudClient;

    @InjectMocks
    private TransactionService transactionService;

    private UUID accountId;
    private UUID cardId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountId = UUID.randomUUID();
        cardId = UUID.randomUUID();
    }

    @Test

    void testCreateTransactionApproved() {
        var card = new CardClient.CardResponse(
                cardId,
                "ACTIVE",
                LocalDate.now().plusYears(1),
                "1234-5678-9012-3456"
        );
        when(cardClient.getCardById(cardId)).thenReturn(card);

        var account = new AccountClient.AccountResponse(
                accountId,
                "ACTIVE",
                BigDecimal.valueOf(500)
        );
        when(accountClient.getAccountById(accountId)).thenReturn(account);

        when(fraudClient.checkFraud(cardId, BigDecimal.valueOf(100)))
                .thenReturn(false);

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction tx = transactionService.createTransaction(accountId, cardId, BigDecimal.valueOf(100), "D");

        assertNotNull(tx);
        assertEquals("APPROVED", tx.getResponse());
        assertEquals(BigDecimal.valueOf(100), tx.getTransactionAmount());
        verify(accountClient).updateBalance(accountId, BigDecimal.valueOf(400));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


    @Test
    void testCreateTransactionFraudulent() {
        var card = new CardClient.CardResponse(
                cardId,
                "ACTIVE",
                LocalDate.now().plusYears(1),
                "1234-5678-9012-3456"
        );
        when(cardClient.getCardById(cardId)).thenReturn(card);

        var account = new AccountClient.AccountResponse(
                accountId,
                "ACTIVE",
                BigDecimal.valueOf(500)
        );
        when(accountClient.getAccountById(accountId)).thenReturn(account);


        when(fraudClient.checkFraud(cardId, BigDecimal.valueOf(200)))
                .thenReturn(true);

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction tx = transactionService.createTransaction(accountId, cardId, BigDecimal.valueOf(200), "D");

        assertNotNull(tx);
        assertTrue(tx.getResponse().startsWith("REJECTED"));
        verify(accountClient, never()).updateBalance(any(), any());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testInsufficientBalance() {
        var card = new CardClient.CardResponse(
                cardId,
                "ACTIVE",
                LocalDate.now().plusYears(1),
                "1234-5678-9012-3456"
        );
        when(cardClient.getCardById(cardId)).thenReturn(card);

        var account = new AccountClient.AccountResponse(
                accountId,
                "ACTIVE",
                BigDecimal.valueOf(500)
        );
        when(accountClient.getAccountById(accountId)).thenReturn(account);

        when(fraudClient.checkFraud(cardId, BigDecimal.valueOf(100)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                transactionService.createTransaction(accountId, cardId, BigDecimal.valueOf(100), "D"));
    }
}
