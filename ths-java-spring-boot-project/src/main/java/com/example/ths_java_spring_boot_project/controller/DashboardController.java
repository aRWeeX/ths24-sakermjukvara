package com.example.ths_java_spring_boot_project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String dashboard(HttpServletRequest request,
                            Authentication auth,
                            Model model) {

        HttpSession session = request.getSession();

        // Session information
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("sessionCreated", new Date(session.getCreationTime()));
        model.addAttribute("sessionLastAccessed", new Date(session.getLastAccessedTime()));
        model.addAttribute("sessionMaxInactive", session.getMaxInactiveInterval());

        // User information
        model.addAttribute("email", auth.getName());
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());

        return "dashboard";
    }

    @GetMapping("/session-info")
    @ResponseBody
    public Map<String, Object> sessionInfo(HttpServletRequest request, Authentication auth) {
        HttpSession session = request.getSession();

        Map<String, Object> info = new HashMap<>();

        info.put("sessionId", session.getId());
        info.put("username", auth.getName());
        info.put("creationTime",  new Date(session.getCreationTime()));
        info.put("lastAccessedTime", new Date(session.getLastAccessedTime()));
        info.put("maxInactiveInterval", session.getMaxInactiveInterval());
        info.put("isNew", session.isNew());

        return info;
    }
}
