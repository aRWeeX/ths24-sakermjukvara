package com.example.ths_java_spring_boot_project.config.api;

import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.payload.ApiResponse;
import com.example.ths_java_spring_boot_project.payload.JwtResponse;
import com.example.ths_java_spring_boot_project.payload.LoginRequest;
import com.example.ths_java_spring_boot_project.payload.RegisterRequest;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import com.example.ths_java_spring_boot_project.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // In-memory store for refresh token rotation
    private final Map<String, String> validRefreshTokens = new ConcurrentHashMap<>();

    private final long accessTokenValidityMs;

    public ApiAuthController(AuthenticationManager authenticationManager,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             JwtUtils jwtUtils,
                             @Value("${app.expiration}") long accessTokenValidityMs) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.accessTokenValidityMs = accessTokenValidityMs;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        // 1. Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Extract username and role
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // 3. Generate access token (short-lived)
        String accessToken = jwtUtils.generateAccessToken(authentication);

        // 4. Generate refresh token with different lifetimes
        Duration refreshDuration = loginRequest.isRememberMe()
                ? Duration.ofDays(14)   // "remember me"
                : Duration.ofHours(2);  // normal short-lived

        String refreshToken = jwtUtils.generateRefreshToken(username, refreshDuration, loginRequest.isRememberMe());

        // 5. Store refresh token for rotation
        validRefreshTokens.put(username, refreshToken);

        // 6. Set HttpOnly cookie with refresh token
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/api/auth")
                .maxAge(refreshDuration)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 7. Return access token in JSON
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken, username, Collections.singletonList(role)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        // 1. Extract refresh token from HttpOnly cookie
        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No refresh token cookie found"));

        // 2. Validate the token
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. Extract username and role from token
        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
        String role = jwtUtils.getRoleFromJwtToken(refreshToken);
        boolean rememberMe = jwtUtils.getRememberMeFromJwtToken(refreshToken);

        // 4. Check token rotation: it must match the stored latest refresh token
        String storedToken = validRefreshTokens.get(username);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 5. Generate new access token (short-lived)
        String newAccessToken = jwtUtils.generateAccessToken(username, role, accessTokenValidityMs);

        // 6. Generate new refresh token with same rememberMe duration
        Duration refreshDuration = rememberMe ? Duration.ofDays(14) : Duration.ofHours(2);
        String newRefreshToken = jwtUtils.generateRefreshToken(username, refreshDuration, rememberMe);

        // 7. Update stored token for rotation
        validRefreshTokens.put(username, newRefreshToken);

        // 8. Update HttpOnly cookie with new refresh token
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .path("/api/auth")
                .maxAge(refreshDuration)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 9. Return the new access token in JSON
        return ResponseEntity.ok(new JwtResponse(
                newAccessToken, newRefreshToken, username, Collections.singletonList(role)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Error: Email is already registered!", null));
        }

        User user = new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>("User registered successfully!", null));
    }
}
