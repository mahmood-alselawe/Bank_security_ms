package com.takarub.AuthJwtTemplate.exceptionHandler;

import com.takarub.AuthJwtTemplate.dto.ErrorResponse;
import com.takarub.AuthJwtTemplate.exception.AccountLockedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Map to hold field-specific error messages
        Map<String, String> errors = new HashMap<>();

        // Loop through all errors to extract field names and their default messages
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();  // Get the default validation message
            errors.put(fieldName, errorMessage);  // Store field and message in the map
        });

        // Construct the ErrorResponse
        ErrorResponse.Error errorDetails = new ErrorResponse.Error(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed"
        );

        // Build the response with field errors if any
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .error(errorDetails)
                .build();

        // Only add fieldErrors if there are any (skip if empty)
        if (!errors.isEmpty()) {
            errorResponse.setFieldErrors(errors);
        }

        // Return the response with the custom error message
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptionCustom(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(new ErrorResponse.Error(
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        ex.getMessage()
                                ))
                                .build()
                );
    }
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountLockedException(AccountLockedException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.LOCKED.value())
                                .error(new ErrorResponse.Error(
                                        HttpStatus.LOCKED.value(),
                                        ex.getMessage()
                                ))
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        // No field-specific errors, so we don't need to set `fieldErrors`
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .error(new ErrorResponse.Error(
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad credentials"
                ))
                .fieldErrors(null)  // Explicitly set fieldErrors to null
                .build();

        // Only include fieldErrors if needed, in this case no fieldErrors for BadCredentialsException
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
//    public ResponseEntity<ErrorResponse> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .success(false)
//                .status(HttpStatus.FORBIDDEN.value())
//                .error(new ErrorResponse.Error(
//                        HttpStatus.FORBIDDEN.value(),
//                        "You do not have permission to access this resource."
//                ))
//                .fieldErrors(null) // No field-specific errors in this case
//                .build();
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
}
