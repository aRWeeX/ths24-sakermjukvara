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
        User user1 = new User();
        user1.setFirstName("User");
        user1.setLastName("User");
        user1.setEmail("user@example.com");
        user1.setHashedPassword(passwordEncoder.encode("user123"));
        user1.setRole("USER");
        user1.setEnabled(true);

        User user2 = new User();
        user2.setFirstName("Admin");
        user2.setLastName("Admin");
        user2.setEmail("admin@example.com");
        user2.setHashedPassword(passwordEncoder.encode("admin123"));
        user2.setRole("ADMIN");
        user2.setEnabled(true);

        userRepository.save(user1);
        userRepository.save(user2);

        System.out.println("Test users created:");
        System.out.println("- User 1: user@example.com / user123 (USER)");
        System.out.println("- User 2: admin@example.com / admin123 (ADMIN)");
    }
}
