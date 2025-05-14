package com.takarub.AuthJwtTemplate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_Token")
    private String accessToken;
    @JsonProperty("expiration_time")
    private String expirationTime;
    @JsonProperty("issued_at")
    private String issuedAt;
    @JsonProperty("refresh_Token")
    private String refreshToken;
}
