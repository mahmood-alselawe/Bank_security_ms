package com.takarub.AuthJwtTemplate.service;


import com.takarub.AuthJwtTemplate.dto.bankDto.*;

public interface BankService {
    BankResponse createBankAccount(String email, BankAccountRequest bankAccountRequest);
    BankResponse balanceEnquiry(String accountNumber);
    String nameEnquiry(String accountNumber);
    BankResponse depositToAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransFerRequest transFerRequest);
    public String lockAccount( String accountNumber, String reason);
    public String unlockAccount(String accountNumber);

    // bankSatament

    // transActions log
}
