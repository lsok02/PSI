package org.example.securityservice.config;

import org.springframework.context.annotation.Configuration;

// CORS is handled by API Gateway - no need for CORS config here
// When requests come through the gateway, it adds CORS headers
@Configuration
public class SecurityConfig {
    // Empty - CORS removed to avoid duplicate headers with API Gateway
}