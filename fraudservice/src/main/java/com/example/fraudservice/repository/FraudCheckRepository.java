package com.example.fraudservice.repository;

import com.example.fraudservice.entity.FraudCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface FraudCheckRepository extends JpaRepository<FraudCheck, UUID> {
    List<FraudCheck> findByCardIdAndTransactionDateAfter(UUID cardId, Timestamp after);
}
