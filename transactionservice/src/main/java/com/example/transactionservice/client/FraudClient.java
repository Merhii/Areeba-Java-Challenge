package com.example.transactionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@FeignClient(name = "fraud-service", url = "http://localhost:8083/api/fraud")
public interface FraudClient {

    @PostMapping("/check")
    boolean checkFraud(@RequestParam UUID cardId, @RequestParam BigDecimal amount);


}

