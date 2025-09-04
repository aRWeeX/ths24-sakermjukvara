package com.example.ths_java_spring_boot_project.config;

import com.example.ths_java_spring_boot_project.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Value("${security.rememberme.key}")
    private String rememberMeKey;

    // PasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager bean (modern, no DaoAuthenticationProvider)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {

        return authConfig.getAuthenticationManager();
    }

    /*
    `HttpSessionEventPublisher` is a Spring component that publishes session-related events, such as session creation
     and destruction, to the application context.
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /*
    ✅ Recommended config order
    (doesn’t technically matter, but improves readability & avoids surprises):

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
    */

    // API Security (stateless, Basic Auth)
    @Bean
    @Order(1)  // Evaluated first
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")  // Only applies to API endpoints
                .csrf(csrf -> csrf.disable())  // No CSRF for APIs
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )  // Stateless for APIs
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - no authentication required
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/health/**").permitAll()

                        // Reading open to all
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()

                        // Writing requires authentication
                        .requestMatchers(HttpMethod.POST, "/api/books/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").authenticated()

                        // Admin endpoints - requires authentication
                        .requestMatchers("/api/admin/**").authenticated()

                        // All other API endpoints - requires authentication
                        .requestMatchers("/api/**").authenticated()

                        // Fallback - everything else also requires authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());  // Basic Auth for APIs

        return http.build();
    }

    // Web Security (form login, sessions)
    @Bean
    @Order(2)  // Evaluated after API rules
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )  // Allow H2 frames
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )  // Disable CSRF for H2
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login?expired")
                        .sessionFixation(fixation -> fixation.changeSessionId())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/public/**",
                                "/css/**",
                                "/js/**",
                                "/signup",
                                "/register",
                                "/h2-console/**"  // Allow access to H2 console
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard")
                        .permitAll()
                )
                // Non-Persistent "Remember Me" (without database):
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
