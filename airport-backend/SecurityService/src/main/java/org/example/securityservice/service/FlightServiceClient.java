package org.example.securityservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.FlightDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FlightServiceClient {

    private final WebClient flightServiceWebClient;

    public FlightServiceClient() {
        this.flightServiceWebClient = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public boolean lockFlightsForTerminalAndDate(LocalDate date, String terminal) {
        try {
            log.info("Calling flight service to lock flights for terminal: {}, date: {}", terminal, date);

            Map<String, Object> requestBody = Map.of(
                    "date", date.toString(),
                    "terminal", terminal
            );

            Map<String, Object> response = flightServiceWebClient.post()
                    .uri("/api/flights/lock-and-delay")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                log.info("Flight service response: {}", response);
                return true;
            }

            return false;

        } catch (WebClientResponseException e) {
            log.error("Error calling flight service: HTTP {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to lock flights: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error communicating with flight service: {}", e.getMessage());
            throw new RuntimeException("Flight service unavailable");
        }
    }


    public boolean unlockFlightsForTerminalAndDate(LocalDate date, String terminal) {
        try {
            log.info("Calling flight service to unlock flights for terminal: {}, date: {}", terminal, date);

            Map<String, Object> requestBody = Map.of(
                    "date", date.toString(),
                    "terminal", terminal
            );

            Map<String, Object> response = flightServiceWebClient.post()
                    .uri("/api/flights/unlock")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                log.info("Flight service unlock response: {}", response);
                return true;
            }

            return false;

        } catch (WebClientResponseException e) {
            log.error("Error calling flight service unlock: HTTP {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to unlock flights: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error communicating with flight service: {}", e.getMessage());
            throw new RuntimeException("Flight service unavailable");
        }
    }
}