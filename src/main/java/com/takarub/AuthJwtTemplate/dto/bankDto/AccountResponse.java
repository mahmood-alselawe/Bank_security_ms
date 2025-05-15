package com.takarub.AuthJwtTemplate.dto.bankDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private Long id;
    private String address;
    private String stateOfOrigin;
    private AccountInfo accountInfo;
    private String gender;
    private String status;
    private boolean locked;
    private String imageUrl;

}
