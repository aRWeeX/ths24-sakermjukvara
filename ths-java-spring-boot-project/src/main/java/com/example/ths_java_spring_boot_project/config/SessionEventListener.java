package com.example.ths_java_spring_boot_project.config;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SessionEventListener.class);

    @EventListener
    public void handleSessionCreated(HttpSessionCreatedEvent event) {
        HttpSession session = event.getSession();
        logger.info("Session created: {}", session.getId());
    }

    @EventListener
    public void handleSessionDestroyed(HttpSessionDestroyedEvent event) {
        HttpSession session = event.getSession();
        logger.info("Session destroyed: {}", session.getId());
    }

    @EventListener
    public void handleSessionAuthentication(InteractiveAuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        logger.info("User logged in: {}", auth.getName());
    }

    @EventListener
    public void handleLogout(LogoutSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        logger.info("User logged out: {}", auth.getName());
    }
}
