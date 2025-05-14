package com.takarub.AuthJwtTemplate.config;

import com.takarub.AuthJwtTemplate.exception.CustomAccessDeniedHandler;
import com.takarub.AuthJwtTemplate.model.Role;
import com.takarub.AuthJwtTemplate.service.LogoutService;
import com.takarub.AuthJwtTemplate.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.takarub.AuthJwtTemplate.model.Permission.*;
import static com.takarub.AuthJwtTemplate.model.Role.ADMIN;
import static com.takarub.AuthJwtTemplate.model.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class applicationSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserDetailsImpl userDetailsServiceImpl;

    private final LogoutService logoutService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        //                        .requestMatchers("").hasAuthority("")
                        .requestMatchers("/api/v1/Management/**").hasAnyRole(ADMIN.name(),MANAGER.name())

                        .requestMatchers(GET,"/api/v1/Management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                        .requestMatchers(POST,"/api/v1/Management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/Management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/Management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

//                        .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
//
//                        .requestMatchers(GET,"/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//                        .requestMatchers(POST,"/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
//                        .requestMatchers(PUT,"/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                        .requestMatchers(DELETE,"/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())




                        .anyRequest()
                        .authenticated()
                ).userDetailsService(userDetailsServiceImpl)
                .exceptionHandling(
                        ex->ex.accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutService)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }






}
