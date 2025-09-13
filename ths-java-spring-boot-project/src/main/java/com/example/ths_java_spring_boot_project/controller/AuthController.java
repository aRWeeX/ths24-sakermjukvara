package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.payload.RegisterRequest;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    @ResponseBody
    public Resource showLoginForm() {
        return new ClassPathResource("static/login.html");
    }

    @GetMapping("/signup")
    @ResponseBody
    public Resource showSignupForm() {
        return new ClassPathResource("static/signup.html");
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest,
                                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Collect all validation errors
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));

            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error",
                    "User already exists: " + registerRequest.getEmail()
            ));
        }

        User newUser = new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}
