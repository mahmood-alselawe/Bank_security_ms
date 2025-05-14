package com.takarub.AuthJwtTemplate.dto.bankDto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransFerRequest {

    private String sourceAccountNumber;

    private String destinationAccountNumber;

    private BigDecimal amount;

    private String otp;
}
