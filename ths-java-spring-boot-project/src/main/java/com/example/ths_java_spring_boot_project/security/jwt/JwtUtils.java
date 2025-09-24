package com.example.ths_java_spring_boot_project.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final SecretKey key;
    private final long accessTokenValidityMs;
    private final long refreshTokenValidityMs;

    public JwtUtils(@Value("${app.secret}") String secret,
                    @Value("${app.expiration}") long expiration,
                    @Value("${app.refreshExpiration}") long refreshExpiration) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityMs = expiration;
        this.refreshTokenValidityMs = refreshExpiration;
    }

    // Generate access token
    public String generateAccessToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        String role = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");  // Fallback. Safety net

        return generateAccessToken(userPrincipal.getUsername(), role, accessTokenValidityMs);
    }

    // Overloading
    public String generateAccessToken(String username, String role, long accessTokenValidityMs) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessTokenValidityMs)))
                .signWith(key)
                .compact();
    }

    // Generate refresh token
    public String generateRefreshToken(String username, Duration duration, boolean rememberMe) {
        Instant now = Instant.now();
        Instant expiry = now.plus(duration);

        return Jwts.builder()
                .subject(username)
                .claim("rememberMe", rememberMe)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            parser.parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        Jws<Claims> jwsClaims = parser.parseSignedClaims(token);
        Claims claims = jwsClaims.getPayload();
        return claims.getSubject();
    }

    public String getRoleFromJwtToken(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        Jws<Claims> jwsClaims = parser.parseSignedClaims(token);
        Claims claims = jwsClaims.getPayload();
        return claims.get("role", String.class);
    }

    public Instant getExpirationInstantFromToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            Jws<Claims> jwsClaims = parser.parseSignedClaims(token);
            Claims claims = jwsClaims.getPayload();
            return claims.getExpiration().toInstant();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }

    public boolean getRememberMeFromJwtToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            Jws<Claims> jwsClaims = parser.parseSignedClaims(token);
            Claims claims = jwsClaims.getPayload();

            Boolean rememberMeClaim = claims.get("rememberMe", Boolean.class);
            return rememberMeClaim != null && rememberMeClaim;
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid token
            return false;
        }
    }
}
