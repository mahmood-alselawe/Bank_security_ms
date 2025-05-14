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
public class BankAccountRequest {

    private String otherName;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private String status;
}
