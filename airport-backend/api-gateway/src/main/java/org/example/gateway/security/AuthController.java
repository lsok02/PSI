package org.example.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import java.util.Properties;
import java.io.FileInputStream;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private Properties users = new Properties();

    @Value("${config.path:../config/users.properties}")
    private String configPath;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void init() {
        try {
            users.load(new FileInputStream(configPath));
        } catch (Exception e) {
            // Domyślni użytkownicy
            users.setProperty("admin", "admin123");
            users.setProperty("pilot", "pilot123");
            users.setProperty("staff", "staff123");
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String storedPassword = users.getProperty(request.username());

        if (storedPassword != null && storedPassword.equals(request.password())) {
            String token = jwtUtil.generateToken(request.username());
            System.out.println("Login successful");
            return new LoginResponse(token, "Login successful");
        }

        return new LoginResponse(null, "Invalid credentials");
    }

    // DTO
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token, String message) {}
}