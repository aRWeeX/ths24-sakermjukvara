package com.example.ths_java_spring_boot_project.security;

import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class LoginAttemptService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final UserRepository userRepository;

    public LoginAttemptService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isBlocked(User user) {
        return user.getLockUntil() != null && Instant.now().isBefore(user.getLockUntil());
    }

    public void loginSucceeded(User user) {
        user.setFailedLoginAttempts(0);
        user.setLockUntil(null);
        userRepository.save(user);
    }

    public void loginFailed(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setLockUntil(Instant.now().plus(LOCK_DURATION));
        }

        userRepository.save(user);
    }
}
