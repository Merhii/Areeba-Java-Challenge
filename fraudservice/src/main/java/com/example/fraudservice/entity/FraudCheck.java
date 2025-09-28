package com.example.fraudservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "fraud_checks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FraudCheck {

    @Id
    private UUID id;

    private UUID cardId;
    private BigDecimal transactionAmount;
    private Timestamp transactionDate;

    private boolean fraudulent;
}
