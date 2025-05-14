package com.takarub.AuthJwtTemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class changePasswordRequest {

    private String currentPassword;

    private String newPassword;

    private String confirmationPassword;
}
