package org.example.groundopsservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${integration.auth.base-url:http://localhost:9090}")
    private String authBaseUrl;

    public String getUsernameFromToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        try {
            String url = authBaseUrl + "/api/auth/username";
            HttpHeaders headers = new HttpHeaders();
            headers.add("token", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (Exception ex) {
            log.error("Failed to resolve username from token", ex);
            return null;
        }
    }
}
