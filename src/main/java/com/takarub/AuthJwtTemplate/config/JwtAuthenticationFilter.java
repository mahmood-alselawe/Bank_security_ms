package com.takarub.AuthJwtTemplate.config;

import com.takarub.AuthJwtTemplate.repository.TokenRepository;
import com.takarub.AuthJwtTemplate.service.JwtServices;
import com.takarub.AuthJwtTemplate.service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServices jwtServices;
    private final TokenRepository tokenRepository;

    private final UserDetailsImpl userDetailsImpl;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    // extract data from request
                                    @NonNull HttpServletResponse response,
                                    // tp provide/return data
                                    @NonNull FilterChain filterChain
                                    //have all filter that we need it
    ) throws ServletException, IOException {
        // first check - check if1 have token or not
        // token in stander the token placed in header
        // in Authorization its came after this word Bearer Token
        //1- we should extract them from herder using request
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;


        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        jwtToken = authHeader.substring(7);

        // as we talked after check if have token we use extract subject from token
        // using this subject we will search about this user in database and return
        // data using  UserDetailsService if did not have user in database will
        //throw exception user not found  so here we need JwtServices to extract Email from Token
        userEmail = jwtServices.extractUsername(jwtToken);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails1 = userDetailsImpl.loadUserByUsername(userEmail);
            // here we reach to stage that we get user info from database using userDetailsImpl
            // + emailFromToken

            //===================
            // new code this code to check about Token if expired abd invoke

            var isTokenValid = tokenRepository.findByToken(jwtToken)
                    .map(t->!t.isExpired() && !t.isRevoked())
                    .orElse(false);

            //=====================
            if (jwtServices.isValid(jwtToken,userDetails1) && isTokenValid){
                // here we reach to step check if token is valid or not
                // if yes we should to update SecurityContextHolder
                // as we talk SecurityContextHolder ues to tell spring this user is
                // authenticated do not check again
                // to do this we use this UsernamePasswordAuthenticationToken
                // instad to update SecurityContextHolder and then the request go to
                // dispatch servlet and then go to controller layer
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails1,
                                null,
                                userDetails1.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);


            }// here can i add else
        }
        filterChain.doFilter(request,response);
    }
}
