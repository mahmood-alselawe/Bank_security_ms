package com.takarub.AuthJwtTemplate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Exclude null fields from serialization
public class ErrorResponse {

    private boolean success;
    private int status;
    private Error error;
    private Map<String, String> fieldErrors;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error{
        private int code;
        private String message;
    }
}
