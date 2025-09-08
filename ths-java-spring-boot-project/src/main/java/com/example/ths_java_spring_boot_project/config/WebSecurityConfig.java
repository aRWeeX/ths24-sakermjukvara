package com.example.ths_java_spring_boot_project.config;

import com.example.ths_java_spring_boot_project.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@Order(2)
public class WebSecurityConfig {

    // Publishes session lifecycle events (needed for concurrency control)
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // Tracks active sessions
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Value("${spring.security.rememberme.key}")
    private String rememberMeKey;

    private final CustomUserDetailsService customUserDetailsService;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

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
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )  // Allow H2 console frames
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )  // Disable CSRF for H2 console
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login?expired")
                        .sessionFixation(fixation -> fixation.changeSessionId())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry())
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/h2-console/**",
                                "/signup",
                                "/register"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard")
                        .permitAll()
                )
                // Non-Persistent "Remember Me" (without database)
                .rememberMe(remember -> remember
                        .key(rememberMeKey)
                        .tokenValiditySeconds(86400 * 7)  // 7 days
                        .userDetailsService(customUserDetailsService)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
