package org.example.securityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.IncidentDTO;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.dto.StatusChangeDTO;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.service.AuthServiceClient;
import org.example.securityservice.service.EmployeeService;
import org.example.securityservice.service.FlightServiceClient;
import org.example.securityservice.service.IncidentService;
import org.example.securityservice.service.SensorEventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/security/incidents")
@RequiredArgsConstructor
@Slf4j
public class IncidentController {

    private final IncidentService incidentService;
    private final SensorEventService sensorEventService;
    private final AuthServiceClient authServiceClient;
    private final EmployeeService employeeService;
    private final FlightServiceClient flightServiceClient;

    @GetMapping
    public ResponseEntity<List<IncidentResponseDTO>> getIncidents(
            @RequestParam(required = false) IncidentStatus status,
            @RequestParam(required = false) IncidentPriority priority,
            @RequestParam(required = false) IncidentType type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestHeader(value = "token", required = false) String token) {

        log.debug("Received request to get incidents with filters - status: {}, priority: {}, type: {}",
                status, priority, type);

        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);

            List<IncidentResponseDTO> incidents = incidentService.getIncidents(
                    status, priority, type, from, to, employee.getId());

            log.info("Returning {} incidents for user {}", incidents.size(), employee.getId());
            return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> getIncident(
            @PathVariable Long id,
            @RequestHeader(value = "token", required = false) String token) {

        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);

        log.debug("Received request to get incident ID: {} for user ID: {}", id, employee.getId());

        try {
            IncidentResponseDTO incident = incidentService.getIncidentById(id, employee.getId());

            if (incident == null) {
                log.warn("Incident not found: {}", id);
                return ResponseEntity.notFound().build();
            }

            log.info("Returning incident: {}", incident.getReportNumber());
            return ResponseEntity.ok(incident);

        } catch (Exception e) {
            log.error("Error retrieving incident {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<IncidentResponseDTO> createIncident(
             @RequestBody IncidentDTO incidentDTO,
             @RequestHeader(value = "token", required = false) String token) {
        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);

        log.info("Received request to create incident by user ID: {}", employee.getId());

        try {
            IncidentResponseDTO createdIncident = incidentService.createIncident(incidentDTO, employee.getId());
            log.info("Incident created successfully: {}", createdIncident.getReportNumber());

            String terminalName = createdIncident.getLocation().getName();
            LocalDateTime creationTime = createdIncident.getCreationTime();
            LocalDate incidentDate = creationTime.toLocalDate();
            boolean flightsLocked = flightServiceClient.lockFlightsForTerminalAndDate(
                    incidentDate,
                    terminalName
            );

            if (flightsLocked) {
                log.info("Successfully locked flights for terminal: {} on date: {}",
                        terminalName, incidentDate);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("X-Incident-Id", createdIncident.getId().toString())
                    .body(createdIncident);

        } catch (Exception e) {
            log.error("Error creating incident: {}", e.getMessage(), e);
            throw e; // GlobalExceptionHandler will handle it
        }
    }

    @PostMapping("/{alarmId}")
    public ResponseEntity<IncidentResponseDTO> createIncidentForAlarm(
            @RequestBody IncidentDTO incidentDTO,
            @PathVariable Long alarmId,
            @RequestHeader(value = "token", required = false) String token) {
        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);
        log.info("Received request to create incident by user ID: {}", employee.getId());

        try {
            IncidentResponseDTO createdIncident = incidentService.createIncident(incidentDTO, employee.getId());

            log.info("Incident created successfully: {}", createdIncident.getReportNumber());

            sensorEventService.addIncidentForAlarm(alarmId, createdIncident);

            String terminalName = createdIncident.getLocation().getName();
            LocalDateTime creationTime = createdIncident.getCreationTime();
            LocalDate incidentDate = creationTime.toLocalDate();
            boolean flightsLocked = flightServiceClient.lockFlightsForTerminalAndDate(
                    incidentDate,
                    terminalName
            );

            if (flightsLocked) {
                log.info("Successfully locked flights for terminal: {} on date: {}",
                        terminalName, incidentDate);
            }
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("X-Incident-Id", createdIncident.getId().toString())
                    .body(createdIncident);

        } catch (Exception e) {
            log.error("Error creating incident: {}", e.getMessage(), e);
            throw e; // GlobalExceptionHandler will handle it
        }
    }

    @PostMapping("/{id}/assign/{teamId}")
    public ResponseEntity<IncidentResponseDTO> assignTeam(
            @PathVariable Long id,
            @PathVariable Long teamId,
            @RequestHeader(value = "token", required = false) String token) {
        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);
        log.info("Received request to assign team to incident {} by user {}", id, employee.getId());

        try {
            IncidentResponseDTO updatedIncident = incidentService.assignTeam(id, teamId, employee.getId());

            log.info("Team assigned to incident {}: team ID {}",
                    updatedIncident.getReportNumber(), teamId);

            return ResponseEntity.ok(updatedIncident);

        } catch (Exception e) {
            log.error("Error assigning team to incident {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<IncidentResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusChangeDTO statusChangeDTO,
            @RequestHeader(value = "token", required = false) String token) {
        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);
        log.info("Received request to update status of incident {} to {} by user {}",
                id, statusChangeDTO.getNewStatus(), employee.getId());

        try {
            IncidentResponseDTO updatedIncident = incidentService.updateStatus(id, statusChangeDTO, employee.getId());

            if(statusChangeDTO.getNewStatus() == IncidentStatus.CLOSED) {

                String terminalName = updatedIncident.getLocation().getName();
                LocalDateTime creationTime = updatedIncident.getCreationTime();
                LocalDate incidentDate = creationTime.toLocalDate();
                boolean flightsUnlocked = flightServiceClient.unlockFlightsForTerminalAndDate(
                        incidentDate,
                        terminalName
                );

                if (flightsUnlocked) {
                    log.info("Successfully locked flights for terminal: {} on date: {}",
                            terminalName, incidentDate);
                }
            }
            log.info("Status updated for incident {}: {} -> {}",
                    updatedIncident.getReportNumber(),
                    updatedIncident.getStatus(),
                    statusChangeDTO.getNewStatus());

            return ResponseEntity.ok(updatedIncident);

        } catch (Exception e) {
            log.error("Error updating status for incident {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PatchMapping("/{id}/escalate")
    public ResponseEntity<IncidentResponseDTO> escalateIncident(
            @PathVariable Long id,
            @RequestHeader(value = "token", required = false) String token) {

        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);

        log.info("Received request to escalate incident ID: {} by user ID: {}", id, employee.getId());

        try {
            IncidentResponseDTO escalatedIncident = incidentService.escalateIncident(id, employee.getId());
            return ResponseEntity.ok(escalatedIncident);
        } catch (Exception e) {
            log.error("Error escalating incident {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

}