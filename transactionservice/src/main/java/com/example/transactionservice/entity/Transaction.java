package com.example.transactionservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal transactionAmount;

    @Column(nullable = false)
    private Timestamp transactionDate;

    @Column(nullable = false)
    private String transactionType; // "C" or "D"

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private UUID cardId;
}
