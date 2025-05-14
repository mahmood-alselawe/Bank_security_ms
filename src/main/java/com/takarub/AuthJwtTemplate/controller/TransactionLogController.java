package com.takarub.AuthJwtTemplate.controller;

import com.takarub.AuthJwtTemplate.model.TransactionLog;
import com.takarub.AuthJwtTemplate.service.bankImpl.TransactionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/bankLog")
@RequiredArgsConstructor
public class TransactionLogController {
    private final TransactionLogService transactionLogService;
    @GetMapping("/transactions")
    public List<TransactionLog> getTransactionLog(@RequestParam String accountNumber) {
        return transactionLogService.getTransactionsForAccount(accountNumber);
    }

}
