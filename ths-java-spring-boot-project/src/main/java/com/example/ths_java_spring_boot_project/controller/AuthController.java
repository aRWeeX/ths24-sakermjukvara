package com.example.ths_java_spring_boot_project.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

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
}
