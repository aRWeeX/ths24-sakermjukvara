package com.example.ths_java_spring_boot_project.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        logger.warn("WEB_ACCESS_DENIED user={} path={} message={}",
                user, request.getRequestURI(), accessDeniedException.getMessage());
        response.sendRedirect("/error/403");  // forwards to custom error page
    }
}
