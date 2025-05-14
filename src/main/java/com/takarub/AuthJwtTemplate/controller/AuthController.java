package com.takarub.AuthJwtTemplate.controller;

import com.takarub.AuthJwtTemplate.dto.AuthenticationResponse;
import com.takarub.AuthJwtTemplate.dto.ChangePasswordForgot;
import com.takarub.AuthJwtTemplate.dto.LoginRequest;
import com.takarub.AuthJwtTemplate.dto.RegisteredRequest;
import com.takarub.AuthJwtTemplate.service.AuthService;
import com.takarub.AuthJwtTemplate.service.ForgotService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ForgotService forgotService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisteredRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));

    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginRequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authService.login(request));

    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        AuthenticationResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<?> verifyMail(@PathVariable String email){
        return forgotService.verifyEmail(email);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp,
                                            @PathVariable String email){
        return forgotService.verifyOtp(otp, email);
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordForgot changePassword,
                                                        @PathVariable String email){
        return forgotService.changePasswordHandler(changePassword,email);
    }


}
