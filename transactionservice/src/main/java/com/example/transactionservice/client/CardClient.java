package com.example.transactionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.UUID;

@FeignClient(name = "card-service", url = "http://localhost:8080/api/cards")
public interface CardClient {

    @GetMapping("/{id}")
    CardResponse getCardById(@PathVariable("id") UUID id);

    record CardResponse(UUID id, String status, LocalDate expiry, String cardNumber) {}
}
