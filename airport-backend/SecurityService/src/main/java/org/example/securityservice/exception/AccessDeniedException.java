package org.example.securityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

    private final String requiredRole;
    private final String userRole;

    public AccessDeniedException() {
        super("Access denied");
        this.requiredRole = null;
        this.userRole = null;
    }

    public AccessDeniedException(String message) {
        super(message);
        this.requiredRole = null;
        this.userRole = null;
    }

    public AccessDeniedException(String requiredRole, String userRole) {
        super(String.format("Access denied. Required role: %s, User role: %s",
                requiredRole, userRole));
        this.requiredRole = requiredRole;
        this.userRole = userRole;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public String getUserRole() {
        return userRole;
    }
}