package com.takarub.AuthJwtTemplate.dto.bankDto;

import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
    private String otp;

}
