package com.example.ths_java_spring_boot_project.config;

import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        User user1 = new User(
                "User",
                "User",
                "user@example.com",
                passwordEncoder.encode("user123")
        );

        User user2 = new User(
                "Admin",
                "Admin",
                "admin@example.com",
                passwordEncoder.encode("admin123")
        );

        userRepository.save(user1);
        userRepository.save(user2);

        System.out.println("Test users created:");
        System.out.println("- User 1: user@example.com / user123 (USER)");
        System.out.println("- User 2: admin@example.com / admin123 (ADMIN)");
    }
}
