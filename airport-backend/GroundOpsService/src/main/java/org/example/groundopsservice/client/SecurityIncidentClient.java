package org.example.groundopsservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.groundopsservice.client.dto.SecurityIncidentRequest;
import org.example.groundopsservice.client.dto.SecurityIncidentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityIncidentClient {

    private final RestTemplate restTemplate;

    @Value("${integration.security.base-url:http://localhost:8084}")
    private String securityBaseUrl;

    public SecurityIncidentResponse createIncident(SecurityIncidentRequest request, String token) {
        String url = securityBaseUrl + "/api/security/incidents";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isBlank()) {
            headers.add("token", token);
        }

        HttpEntity<SecurityIncidentRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<SecurityIncidentResponse> response =
                restTemplate.postForEntity(url, entity, SecurityIncidentResponse.class);

        return response.getBody();
    }
}
