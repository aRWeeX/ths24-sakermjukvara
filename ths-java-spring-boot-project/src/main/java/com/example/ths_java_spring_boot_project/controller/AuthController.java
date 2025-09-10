package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {

        // Validate password strength
        if (password.length() < 8) {
            model.addAttribute("error", "Password length must be at least 8 characters");
            return "signup";
        }

        if (!password.matches(".*[A-Z].*")) {
            model.addAttribute("error", "Password must contain at least 1 capital letter");
            return "signup";
        }

        if (!password.matches(".*[0-9].*")) {
            model.addAttribute("error", "Password must contain at least 1 number");
            return "signup";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "User already exists: " + email);
            return "signup";
        }

        User newUser = new User(
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password)
        );

        userRepository.save(newUser);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
