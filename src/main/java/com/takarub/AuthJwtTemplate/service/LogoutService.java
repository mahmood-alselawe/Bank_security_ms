package com.takarub.AuthJwtTemplate.service;

import com.takarub.AuthJwtTemplate.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication)
    {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwtToken = authHeader.substring(7);

        var storedToken = tokenRepository.findByToken(jwtToken)
                .orElseThrow(()-> new RuntimeException("Token Not found"));

        if (storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }

    }
}
