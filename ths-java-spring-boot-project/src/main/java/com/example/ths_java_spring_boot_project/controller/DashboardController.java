package com.example.ths_java_spring_boot_project.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    @ResponseBody
    public Resource showDashboard() {
        return new ClassPathResource("static/dashboard.html");
    }
}
