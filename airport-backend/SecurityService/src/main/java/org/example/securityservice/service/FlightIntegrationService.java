package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.FlightDTO;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.LogEntry;
import org.example.securityservice.repository.LogEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightIntegrationService {

    private final FlightServiceClient flightServiceClient;
    private final LogEntryRepository logEntryRepository;

    public void checkAffectedFlights(Incident incident) {
        try {
            if (incident.getLocation() != null) {
                List<FlightDTO> affectedFlights = flightServiceClient.getFlightsByLocation(
                        incident.getLocation().getName(), LocalDateTime.now());

                if (!affectedFlights.isEmpty()) {
                    createAffectedFlightsLog(incident, affectedFlights);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to check affected flights for incident {}: {}",
                    incident.getReportNumber(), e.getMessage());
        }
    }

    public void blockFlightOperationsIfNeeded(Incident incident) {
        try {
            if (incident.getLocation() != null && incident.getLocation().getName() != null) {
                flightServiceClient.blockOperationsInZone(
                        incident.getLocation().getName(),
                        "CRITICAL_INCIDENT_" + incident.getType(),
                        incident.getId());

                createFlightBlockLog(incident);
            }
        } catch (Exception e) {
            log.error("Failed to block flight operations for incident {}: {}",
                    incident.getReportNumber(), e.getMessage());
        }
    }

    private void createAffectedFlightsLog(Incident incident, List<FlightDTO> affectedFlights) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .actionTime(LocalDateTime.now())
                .actionDescription("Affected flights identified")
                .build();
        logEntryRepository.save(logEntry);
    }

    private void createFlightBlockLog(Incident incident) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .actionTime(LocalDateTime.now())
                .actionDescription("Flight operations blocked")
                .build();
        logEntryRepository.save(logEntry);
    }
}