package com.takarub.AuthJwtTemplate.controller;

import com.takarub.AuthJwtTemplate.dto.bankDto.*;
import com.takarub.AuthJwtTemplate.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank")
//@Hidden
@RequiredArgsConstructor

public class BankController {

    private final BankService service;
    @PostMapping("/create/{email}")
    public ResponseEntity<BankResponse> createAccount(@PathVariable String email, @RequestBody BankAccountRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBankAccount(email,request));
    }
    @GetMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(@Valid @RequestParam  String accountNumber) {
        return ResponseEntity.status(HttpStatus.FOUND).body(service.balanceEnquiry(accountNumber));
    }
    @GetMapping("/nameEnquiry")
    public ResponseEntity<String> nameEnquiry(@Valid @RequestParam String accountNumber){
        return ResponseEntity.status(HttpStatus.FOUND).body(service.nameEnquiry(accountNumber));
    }
    @PostMapping("/deposit")
    public ResponseEntity<BankResponse> depositToAccount(@RequestBody CreditDebitRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.depositToAccount(request));
    }
    @PostMapping("debit")
    public  ResponseEntity<BankResponse> debitAccount(@RequestBody  CreditDebitRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(service.debitAccount(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<BankResponse> transfer(@RequestBody TransFerRequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.transfer(request));
    }

    @PostMapping("/accounts/lock")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> lockAccount(@RequestParam String accountNumber, @RequestParam String reason ){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.lockAccount(accountNumber,reason));
    }

    @PostMapping("/accounts/unlock")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> unlockAccount(@RequestParam String accountNumber){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.unlockAccount(accountNumber));
    }




}
