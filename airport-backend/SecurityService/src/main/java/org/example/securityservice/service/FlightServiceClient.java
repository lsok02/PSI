package org.example.securityservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.FlightDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlightServiceClient {

    private final RestTemplate restTemplate;

    @Value("${external.loty.service.url}")
    private String flightServiceUrl;

    public List<FlightDTO> getFlightsByLocation(String zoneCode, LocalDateTime time) {
        try {
            String url = flightServiceUrl + "/flights/by-location?zone={zone}&time={time}";

            ResponseEntity<FlightDTO[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    FlightDTO[].class,
                    zoneCode,
                    time.toString()
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            log.error("Error fetching flights by location: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    public void blockOperationsInZone(String zoneCode, String reason, Long incidentId) {
        try {
            String url = flightServiceUrl + "/operations/block";

            BlockOperationsRequest request = new BlockOperationsRequest(
                    zoneCode, reason, incidentId, LocalDateTime.now());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<BlockOperationsRequest> entity = new HttpEntity<>(request, headers);

            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            log.info("Blocked operations in zone: {}", zoneCode);
        } catch (Exception e) {
            log.error("Error blocking operations: {}", e.getMessage());
            throw new RuntimeException("Failed to block flight operations", e);
        }
    }

    @Data
    @AllArgsConstructor
    private static class BlockOperationsRequest {
        private String zoneCode;
        private String reason;
        private Long incidentId;
        private LocalDateTime timestamp;
    }
}