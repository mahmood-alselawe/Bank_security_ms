package com.takarub.AuthJwtTemplate.controller;

import com.takarub.AuthJwtTemplate.dto.changePasswordRequest;
import com.takarub.AuthJwtTemplate.service.UserServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid changePasswordRequest request,
            Principal principal
    ){
        userServices.changePassword(request,principal);
        return ResponseEntity.accepted().build();
    }
    
}
