package com.takarub.AuthJwtTemplate.repository;

import com.takarub.AuthJwtTemplate.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByAccountNumberOrderByTimestampDesc(String accountNumber);

}
