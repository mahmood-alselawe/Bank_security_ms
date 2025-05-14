package com.takarub.AuthJwtTemplate.dto.bankDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EnquiryRequest {

    private String accountNumber;
}
