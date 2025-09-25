package com.example.ths_java_spring_boot_project.security.jwt;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableScheduling
public class RefreshTokenService {

    private final JwtUtils jwtUtils;

    // In-memory store for refresh token rotation
    private final Map<String, String> validRefreshTokens = new ConcurrentHashMap<>();

    public RefreshTokenService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public void storeToken(String username, String token) {
        validRefreshTokens.put(username, token);
    }

    public String getToken(String username){
        return validRefreshTokens.get(username);
    }

    public void removeToken(String username){
        validRefreshTokens.remove(username);
    }

    @Scheduled(fixedRate = 60_000)
    public void purgeExpiredRefreshTokens() {
        validRefreshTokens.entrySet().removeIf(entry -> {
            try {
                Instant expiration = jwtUtils.getExpirationInstantFromToken(entry.getValue());
                return expiration.isBefore(Instant.now());  // expired → remove
            } catch (RuntimeException e) {
                return true;  // invalid token → also remove
            }
        });
    }
}
