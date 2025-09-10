package com.example.ths_java_spring_boot_project.config;

import com.example.ths_java_spring_boot_project.security.jwt.AuthTokenFilter;
import com.example.ths_java_spring_boot_project.security.jwt.JwtUtils;
import com.example.ths_java_spring_boot_project.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Order(1)  // Ensures this config is processed first if multiple security configs exist
public class ApiSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApiSecurityConfig.class);

    private final CustomUserDetailsService userDetailsService;

    public ApiSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Bean for JWT authentication filter that validates tokens for each request
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(JwtUtils jwtUtils) {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    /**
     * Defines the security filter chain for API endpoints
     */
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {
        http
                // Apply this configuration only to /api/** endpoints
                .securityMatcher("/api/**")

                // Disable CSRF because API is stateless (JWT is used)
                .csrf(csrf -> csrf.disable())

                // Stateless session management because JWT handles authentication
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Role-based access control
                .authorizeHttpRequests(authz -> authz
                        // USERs can access only their profile and book APIs
                        .requestMatchers("/api/users/profile").hasRole("USER")
                        .requestMatchers("/api/books/**").hasRole("USER")

                        // ADMINs can access everything under /api/**
                        .requestMatchers("/api/**").hasRole("ADMIN")

                        // Deny all other requests as a safety measure
                        .anyRequest().denyAll()
                )

                // Custom exception handling for unauthorized requests
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request,
                                                   response,
                                                   authenticationException) -> {

                            // Return a JSON response instead of default plain text
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                            Map<String, Object> body = new HashMap<>();

                            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                            body.put("error", "Unauthorized");
                            body.put("message", authenticationException.getMessage());
                            body.put("path", request.getServletPath());

                            // Convert the map to JSON and write it to the response
                            ObjectMapper mapper = new ObjectMapper();

                            try {
                                mapper.writeValue(response.getOutputStream(), body);
                            } catch (IOException e) {
                                logger.error("Failed to write JSON response for unauthorized request", e);
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                            }
                        })
                )

                // Register the JWT filter before Spring Security's username/password filter
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
