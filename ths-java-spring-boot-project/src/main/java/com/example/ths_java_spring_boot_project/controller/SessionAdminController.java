package com.example.ths_java_spring_boot_project.controller;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class SessionAdminController {

    private final SessionRegistry sessionRegistry;

    public SessionAdminController(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/sessions")
    public String viewActiveSessions(Model model) {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        Map<Object, List<SessionInformation>> principalSessions = new HashMap<>();

        for (Object principal : allPrincipals) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            principalSessions.put(principal, sessions);
        }

        model.addAttribute("principalSessions", principalSessions);
        return "admin/sessions";
    }

    @PostMapping("/sessions/{sessionId}/invalidate")
    public String invalidateSession(@PathVariable String sessionId) {
        SessionInformation sessionInfo = sessionRegistry.getSessionInformation(sessionId);

        if (sessionInfo != null) {
            sessionInfo.expireNow();
        }

        return "redirect:/admin/sessions";
    }
}
