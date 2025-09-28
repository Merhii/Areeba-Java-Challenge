package com.example.fraudservice.service;

import com.example.fraudservice.repository.FraudCheckRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class FraudCheckService {


    private final FraudCheckRepository repo;

    public FraudCheckService(FraudCheckRepository repo) {
        this.repo = repo;
    }

    public boolean checkFraud(UUID cardId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(10000)) > 0) {
            return true;
        }

        Timestamp oneHourAgo = Timestamp.from(Instant.now().minus(Duration.ofHours(1)));
        var recent = repo.findByCardIdAndTransactionDateAfter(cardId, oneHourAgo);

        return recent.size() >= 8;
    }
}
