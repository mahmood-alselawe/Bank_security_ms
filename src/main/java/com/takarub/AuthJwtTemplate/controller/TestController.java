package com.takarub.AuthJwtTemplate.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Test")
@PreAuthorize("hasAnyRole('MANAGER','USER')")
@Hidden
public class TestController {


    @GetMapping
    public String Test(){
        return "Test for MANAGER and USER";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('all:read')")
    public String Delete(){
        return "Delete USER ALL:READ&&READ";
    }

    //hasAnyAuthority

    @PostMapping
    @PreAuthorize("hasAnyAuthority('all:read','admin:create')")
    public String POST(){
        return "POST USER ALL AND ADMIN :READ&&READ";
    }

}
