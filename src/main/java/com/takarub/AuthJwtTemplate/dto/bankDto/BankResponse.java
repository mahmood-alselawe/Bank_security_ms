package com.takarub.AuthJwtTemplate.dto.bankDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BankResponse {

    private String responseCode;

    private String responseMessage;

    private AccountInfo accountInfo;
}
