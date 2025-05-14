package com.takarub.AuthJwtTemplate.service.bankImpl;

import com.takarub.AuthJwtTemplate.model.TransactionLog;
import com.takarub.AuthJwtTemplate.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionLogRepository transactionLogRepository;
    public List<TransactionLog> getTransactionsForAccount(String accountNumber) {
        return transactionLogRepository.findByAccountNumberOrderByTimestampDesc(accountNumber);
    }
    public void logTransaction(TransactionLog log) {
        transactionLogRepository.save(log);
    }
}
