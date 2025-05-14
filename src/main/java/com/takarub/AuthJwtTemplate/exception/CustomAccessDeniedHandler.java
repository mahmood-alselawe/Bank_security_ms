package com.takarub.AuthJwtTemplate.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takarub.AuthJwtTemplate.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Build the ErrorResponse object
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .status(HttpServletResponse.SC_FORBIDDEN)
                .error(new ErrorResponse.Error(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Access Denied: You do not have permission to access this resource."
                ))
                .build();

        // Set the response properties
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the JSON response
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
