package com.example.transactionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "account-service", url = "http://localhost:8081/api/accounts")
public interface AccountClient {

    @GetMapping("/{id}")
    AccountResponse getAccountById(@PathVariable("id") UUID id);

    @PutMapping("/{id}")
    AccountResponse updateBalance(@PathVariable("id") UUID id, @RequestParam BigDecimal balance);

    record AccountResponse(UUID id, String status, BigDecimal balance) {}
}
