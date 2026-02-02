package org.example.securityservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class AuthServiceClient {

    private final WebClient authServiceWebClient;

    public AuthServiceClient() {
        this.authServiceWebClient = WebClient.builder()
                .baseUrl("http://localhost:9090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            return authServiceWebClient.get()
                    .uri("/api/auth/username")
                    .header("token", token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException e) {
            log.error("Error validating token: HTTP {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Token validation failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error communicating with auth service: {}", e.getMessage());
            throw new RuntimeException("Auth service unavailable");
        }
    }
}