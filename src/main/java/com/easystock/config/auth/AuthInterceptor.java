package com.easystock.config.auth;

import com.easystock.dto.auth.UserDto;
import com.easystock.entity.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the handler is a method in a controller
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true; // Not a controller method, let it pass
        }

        // 1. Check for the @Auth annotation on the method
        Auth authAnnotation = handlerMethod.getMethodAnnotation(Auth.class);
        if (authAnnotation == null) {
            return true; // No @Auth annotation, public endpoint
        }

        // 2. The endpoint is protected. Check for an active session.
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warn("Access denied to {}: No session found", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authenticated.");
            return false;
        }

        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            log.warn("Access denied to {}: No user in session", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authenticated.");
            return false;
        }

        // 3. The user is authenticated. Now check for authorization (roles).
        List<UserRole> allowedRoles = Arrays.asList(authAnnotation.allowedRoles());
        if (allowedRoles.isEmpty()) {
            return true; // @Auth is present but no roles specified, so just authentication is needed
        }

        if (allowedRoles.contains(user.getRole())) {
            log.info("User {} with role {} accessed {}", user.getUsername(), user.getRole(), request.getRequestURI());
            return true; // User has the required role
        } else {
            log.warn("Access denied for user {} to {}: Role {} is not in the allowed list {}", user.getUsername(), request.getRequestURI(), user.getRole(), allowedRoles);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource.");
            return false; // User does not have the required role
        }
    }
}