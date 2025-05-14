package com.takarub.AuthJwtTemplate.dto;

import lombok.Builder;

@Builder
public record MailBody(String to , String subject , String text) {
}
