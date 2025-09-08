package com.example.ths_java_spring_boot_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class ApiSecurityConfig {

    /*
     ✅ Recommended config order
     (Order doesn’t change behavior for most methods,
      but improves readability and reduces risk of subtle filter chain issues):

     1. headers()               → security headers (e.g. H2 console frames)
     2. csrf()                  → CSRF enable/disable
     3. sessionManagement()     → session policy (stateless, limits, etc.)
     4. authorizeHttpRequests() → access rules
     5. exceptionHandling()     → 401/403 handling
     6. formLogin()/httpBasic()/oauth2Login()/oauth2ResourceServer()
                                → auth mechanisms (choose what fits)

     Optional (use as needed):

       Session-related:
       7. rememberMe()          → persistent login via cookie
       8. logout()              → custom logout URL/handler

       Login flow:
       9. requestCache()        → cache unauthenticated requests for post-login redirect

     // Order helps clarify intent and avoids rare surprises from filter interactions.
     */

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")  // Only applies to API endpoints
                .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless APIs
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )  // Stateless session
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/books/**").hasRole("USER")  // Requires USER (or ADMIN via hierarchy)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Admin requires role
                        .requestMatchers("/api/**").hasRole("ADMIN")  // All other API endpoints admin only
                );

        return http.build();
    }
}
