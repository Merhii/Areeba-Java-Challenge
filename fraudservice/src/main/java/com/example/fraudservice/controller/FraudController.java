package com.example.fraudservice.controller;

import com.example.fraudservice.service.FraudCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudCheckService service;

    public FraudController(FraudCheckService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkFraud(@RequestParam UUID cardId,
                                              @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(service.checkFraud(cardId, amount));
    }
}
