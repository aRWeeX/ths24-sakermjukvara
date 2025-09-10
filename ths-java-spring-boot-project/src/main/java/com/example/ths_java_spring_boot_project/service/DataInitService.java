package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initData() {
        // Create test users if they don't exist
        if (!userRepository.existsByEmail("user@test.com")) {
            User user = new User(
                    "Test",
                    "User",
                    "user@test.com",
                    passwordEncoder.encode("password123")
            );

            userRepository.save(user);
        }

        if (!userRepository.existsByEmail("admin@test.com")) {
            User user = new User(
                    "Test",
                    "Admin",
                    "admin@test.com",
                    passwordEncoder.encode("admin123")
            );

            userRepository.save(user);
        }

        System.out.println("✅ Test users created:");
        System.out.println("    👤 user@test.com / password123 (USER)");
        System.out.println("    👑 admin@test.com / admin123 (ADMIN)");
    }
}
