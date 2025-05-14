package com.takarub.AuthJwtTemplate.dto;

import lombok.Builder;

@Builder
public record ChangePasswordForgot(String password, String repeatPassword) {
}
