package com.takarub.AuthJwtTemplate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takarub.AuthJwtTemplate.dto.bankDto.*;
import com.takarub.AuthJwtTemplate.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
    @PostMapping("create/account/{email}")
    public ResponseEntity<BankResponse> createBankAccountWithImage(
            @PathVariable("email") String email,
            @RequestPart("bankRequest") String bankRequest,
            @RequestPart("file")MultipartFile file
            ){
        ObjectMapper objectMapper = new ObjectMapper();
        BankAccountRequest request = null;
        try {
            request = objectMapper.readValue(bankRequest,BankAccountRequest.class);
        }catch (JsonProcessingException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Test");
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.createBankAccountWithImage(email,request,file));

    }

    @GetMapping("/findByAccountNumber")
    public ResponseEntity<AccountResponse> findByAccountNumber(@RequestParam String accountNumber){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(service.findByAccountNumber(accountNumber));
    }



}
