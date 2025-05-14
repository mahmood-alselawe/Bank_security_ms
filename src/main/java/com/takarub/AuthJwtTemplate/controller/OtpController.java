package com.takarub.AuthJwtTemplate.controller;

import com.takarub.AuthJwtTemplate.service.bankImpl.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService service;


    @PostMapping("/generate")
    public ResponseEntity<String> generateOtpForApi(@RequestParam String accountNumber){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateOtpForApi(accountNumber));
    }


}
