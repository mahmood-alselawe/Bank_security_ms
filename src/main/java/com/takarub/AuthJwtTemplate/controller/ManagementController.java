package com.takarub.AuthJwtTemplate.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Management")
@RequiredArgsConstructor
@SecurityRequirement(
        name = "bearerAuth"
)
// to change name of controller in swagger
@Tag(name = "Management")
@Hidden
public class ManagementController {

    @GetMapping
    // if descrip this endpoint in clear way
    @Operation(
            description = "get endpoint for manage and admin",
            summary = "this summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "token not valid",
                            responseCode = "403"
                    )
            }
    )
    public String Get(){
        return "GET Management:READ&&READ";
    }

    @PostMapping
    public String Post(){
        return "Post Management:READ&&READ";
    }

    @PutMapping
    public String Put(){
        return "Put Management:READ&&READ";
    }

    @DeleteMapping
    public String Delete(){
        return "Delete Management:READ&&READ";
    }
}
