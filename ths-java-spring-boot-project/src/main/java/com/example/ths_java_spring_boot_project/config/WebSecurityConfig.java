package com.example.ths_java_spring_boot_project.config;

import com.example.ths_java_spring_boot_project.security.WebAccessDeniedHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    /**
     * Security configuration for web endpoints (non-API), e.g., H2 console
     */
    /*
     * Recommended order of HttpSecurity configuration:
     *
     * 1. CSRF / CORS
     *    - Configure CSRF protection (enable/disable/custom)
     *    - Configure CORS if needed
     *
     * 2. Exception handling / Session management / Security context
     *    - authenticationEntryPoint, accessDeniedHandler
     *    - stateless sessions, etc.
     *
     * 3. Authorization rules
     *    - .authorizeHttpRequests()
     *    - PermitAll / Authenticated / Role-based
     *
     * 4. Authentication mechanisms
     *    - .httpBasic()
     *    - .formLogin()
     *    - .oauth2Login()
     *    - .logout()
     *
     * 5. Headers
     *    - Security headers (HSTS, CSP, X-Frame-Options, etc.)
     *
     * Note:
     * - Some DSL methods are order-sensitive (CSRF, authorizeHttpRequests).
     * - Others (headers, httpBasic, logout) are less strict but kept last for clarity.
     */
    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity http, WebAccessDeniedHandler webAccessDeniedHandler)
            throws Exception {

        http
                // Allow frames from the same origin, needed for H2 console UI to work
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )


                // Disable CSRF only for H2 console requests to avoid form errors
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // Role/authorization configuration
                .authorizeHttpRequests(authz -> authz
                        // Public
                        .requestMatchers("/css/**", "/js/**", "/login", "/signup", "/dashboard").permitAll()

                        // Allow anyone to access H2 console
                        .requestMatchers("/h2-console/**").permitAll()

                        // Require login for all other pages
                        .anyRequest().authenticated()
                )

                // Handle unauthorized access with a friendly HTML page and logs the attempt
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(webAccessDeniedHandler)  // 403 HTML
                );

        return http.build();
    }
}
