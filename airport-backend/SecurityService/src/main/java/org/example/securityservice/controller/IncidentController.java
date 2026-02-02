package org.example.securityservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.IncidentDTO;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.dto.LogEntryDTO;
import org.example.securityservice.model.dto.SensorEventDTO;
import org.example.securityservice.model.dto.StatusChangeDTO;
import org.example.securityservice.model.dto.TeamAssignmentDTO;
import org.example.securityservice.model.entity.AuditLog;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.LogEntry;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.service.AuditLogService;
import org.example.securityservice.service.AuthServiceClient;
import org.example.securityservice.service.EmployeeService;
import org.example.securityservice.service.FlightServiceClient;
import org.example.securityservice.service.IncidentService;
import org.example.securityservice.service.NotificationService;
import org.example.securityservice.service.SensorEventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/security/incidents")
@RequiredArgsConstructor
@Slf4j
public class IncidentController {

    private final IncidentService incidentService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;
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
        Long employeeId = null;
        if (token != null && !token.isBlank()) {
            String username = authServiceClient.validateTokenAndGetUsername(token);
            Employee employee = employeeService.getEmployeeByUsername(username);
            if (employee != null) {
                employeeId = employee.getId();
            }
        }

        log.info("Received request to create incident by user ID: {}", employeeId);

        try {
            IncidentResponseDTO createdIncident = incidentService.createIncident(incidentDTO, employeeId);
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
        Long employeeId = null;
        if (token != null && !token.isBlank()) {
            String username = authServiceClient.validateTokenAndGetUsername(token);
            Employee employee = employeeService.getEmployeeByUsername(username);
            if (employee != null) {
                employeeId = employee.getId();
            }
        }
        log.info("Received request to create incident by user ID: {}", employeeId);

        try {
            IncidentResponseDTO createdIncident = incidentService.createIncident(incidentDTO, employeeId);

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






































//
//    @PostMapping("/sensor-events")
//    public ResponseEntity<IncidentResponseDTO> handleSensorEvent(
//             @RequestBody SensorEventDTO sensorEvent) {
//
//        log.info("Received sensor event: {} in zone {}",
//                sensorEvent.getAlarmType(), sensorEvent.getZoneCode());
//
//        try {
////            IncidentResponseDTO createdIncident = incidentService.createIncidentFromSensor(sensorEvent);
//            IncidentResponseDTO createdIncident = new IncidentResponseDTO();
//            log.info("Sensor incident created: {}", createdIncident.getReportNumber());
//
//            return ResponseEntity
//                    .status(HttpStatus.CREATED)
//                    .header("X-Incident-Id", createdIncident.getId().toString())
//                    .body(createdIncident);
//
//        } catch (Exception e) {
//            log.error("Error processing sensor event: {}", e.getMessage(), e);
//            throw e;
//        }
//    }

    // ========== INCIDENT RETRIEVAL ==========
//
//    @GetMapping("/my-team")
//    public ResponseEntity<List<IncidentResponseDTO>> getMyTeamIncidents(
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.debug("Received request to get team incidents for user ID: {}", userId);
//
//        try {
////            List<IncidentResponseDTO> incidents = incidentService.getIncidentsForTeamMember(userId);
//            List<IncidentResponseDTO> incidents = new ArrayList<>();
//
//            log.info("Returning {} incidents for team member {}", incidents.size(), userId);
//            return ResponseEntity.ok(incidents);
//
//        } catch (Exception e) {
//            log.error("Error retrieving team incidents: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//
//
//    @GetMapping("/report/{reportNumber}")
//    public ResponseEntity<IncidentResponseDTO> getIncidentByReportNumber(
//            @PathVariable String reportNumber,
//            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
//
//        log.debug("Received request to get incident by report number: {}", reportNumber);
//
//        try {
//            // This would require a new method in service or repository
//            // For now, we'll redirect to a placeholder
//            throw new UnsupportedOperationException("Get by report number not implemented yet");
//
//        } catch (Exception e) {
//            log.error("Error retrieving incident by report number {}: {}", reportNumber, e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    // ========== INCIDENT MANAGEMENT ==========
//
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<IncidentResponseDTO> updateIncident(
//            @PathVariable Long id,
//            @RequestBody IncidentUpdateDTO updateDTO,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.info("Received request to update incident {} by user {}", id, userId);
//
//        try {
//            // This would require a new method in service
//            // For now, we'll redirect to a placeholder
//            throw new UnsupportedOperationException("Partial update not implemented yet");
//
//        } catch (Exception e) {
//            log.error("Error updating incident {}: {}", id, e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteIncident(
//            @PathVariable Long id,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.warn("Received request to delete incident {} by user {}", id, userId);
//
//        try {
//            // Incidents should not be deleted, only closed
//            // This endpoint might be disabled in production
//            throw new UnsupportedOperationException("Delete operation not allowed for incidents");
//
//        } catch (Exception e) {
//            log.error("Error deleting incident {}: {}", id, e.getMessage(), e);
//            throw e;
//        }
//    }

//    @PostMapping("/{id}/escalate")
//    public ResponseEntity<IncidentResponseDTO> escalateIncident(
//            @PathVariable Long id,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.info("Received request to escalate incident {} by user {}", id, userId);
//
//        try {
//            // This would require a new method in service
//            // For now, we'll redirect to a placeholder
//            throw new UnsupportedOperationException("Manual escalation not implemented yet");
//
//        } catch (Exception e) {
//            log.error("Error escalating incident {}: {}", id, e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    @PostMapping("/{id}/reassign")
//    public ResponseEntity<IncidentResponseDTO> reassignTeam(
//            @PathVariable Long id,
//            @RequestBody TeamAssignmentDTO assignmentDTO,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.info("Received request to reassign team for incident {} by user {}", id, userId);
//
//        try {
//            // This would require a new method in service
//            // For now, we'll redirect to a placeholder
//            throw new UnsupportedOperationException("Reassignment not implemented yet");
//
//        } catch (Exception e) {
//            log.error("Error reassigning team for incident {}: {}", id, e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    // ========== AUDIT & LOGS ==========
//
//    @GetMapping("/{id}/audit-logs")
//    public ResponseEntity<List<AuditLogDTO>> getIncidentAuditLogs(
//            @PathVariable Long id,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.debug("Received request to get audit logs for incident {} by user {}", id, userId);
//
//        try {
////            List<AuditLog> auditLogs = incidentService.getIncidentAuditLogs(id, userId);
//            List<AuditLog> auditLogs = new ArrayList<>();
//
//            List<AuditLogDTO> auditLogDTOs = auditLogs.stream()
//                    .map(this::convertToAuditLogDTO)
//                    .toList();
//
//            log.info("Returning {} audit logs for incident {}", auditLogDTOs.size(), id);
//            return ResponseEntity.ok(auditLogDTOs);
//
//        } catch (Exception e) {
//            log.error("Error retrieving audit logs for incident {}: {}", id, e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    @GetMapping("/{id}/journal")
//    public ResponseEntity<List<LogEntryDTO>> getIncidentJournal(
//            @PathVariable Long id,
//            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
//
//        log.debug("Received request to get journal for incident {} by user {}", id, userId);
//
//        try {
//            // Get incident first to check permissions
//            IncidentResponseDTO incident = incidentService.getIncidentById(id, userId);
//
//            // Journal entries are included in incident response
//            // But we could create a separate endpoint if needed
//            List<LogEntryDTO> journalEntries = incident.getJournalEntries();
//
//            log.info("Returning {} journal entries for incident {}",
//                    journalEntries != null ? journalEntries.size() : 0, id);
//
//            return ResponseEntity.ok(journalEntries);
//
//        } catch (Exception e) {
//            log.error("Error retrieving journal for incident {}: {}", id, e.getMessage(), e);
//            throw e;
//        }
//    }

    @PostMapping("/{id}/journal")
    public ResponseEntity<Void> addJournalEntry(
            @PathVariable Long id,
            @RequestBody JournalEntryCreateDTO entryDTO,
            @RequestHeader(value = "token", required = false) String token) {

        String username = authServiceClient.validateTokenAndGetUsername(token);
        Employee employee = employeeService.getEmployeeByUsername(username);


        log.info("Received request to add journal entry to incident {} by user {}", id, employee.getId());

        try {
            // This would require a new method in service
            // For now, we'll redirect to a placeholder
            throw new UnsupportedOperationException("Manual journal entry not implemented yet");

        } catch (Exception e) {
            log.error("Error adding journal entry to incident {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ========== NOTIFICATIONS ==========
//
//    @GetMapping("/{id}/notifications")
//    public ResponseEntity<List<NotificationDTO>> getIncidentNotifications(
//            @PathVariable Long id,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.debug("Received request to get notifications for incident {} by user {}", id, userId);
//
//        try {
//            // This would require a new method in service
//            // For now, we'll redirect to a placeholder
//            throw new UnsupportedOperationException("Notification retrieval not implemented yet");
//
//        } catch (Exception e) {
//            log.error("Error retrieving notifications for incident {}: {}", id, e.getMessage(), e);
//            throw e;
//        }
//    }
//
//
//
//    // ========== STATISTICS & REPORTS ==========
//
//
//    @GetMapping("/statistics/monthly")
//    public ResponseEntity<MonthlyStatisticsDTO> getMonthlyStatistics(
//            @RequestParam int year,
//            @RequestParam int month,
//            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
//
//        log.debug("Received request for monthly statistics for {}-{}", year, month);
//
//        try {
//            // This would require a new method in service
//            // For now, we'll return mock data
//            throw new UnsupportedOperationException("Monthly statistics not implemented yet");
//
//        } catch (Exception e) {
//            log.error("Error retrieving monthly statistics: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    @GetMapping("/reports/generate")
//    public ResponseEntity<ReportDTO> generateReport(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
//            @RequestParam(required = false) IncidentType type,
//            @RequestParam(required = false) IncidentPriority priority,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.info("Received request to generate report from {} to {} by user {}", from, to, userId);
//
//        try {
//            // This would require a new method in service
//            // For now, we'll redirect to a placeholder
//            throw new UnsupportedOperationException("Report generation not implemented yet");
//
//        } catch (Exception e) {
//            log.error("Error generating report: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    // ========== HEALTH & STATUS ==========
//
//    @GetMapping("/status/summary")
//    public ResponseEntity<IncidentStatusSummaryDTO> getStatusSummary(
//            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
//
//        log.debug("Received request for status summary");
//
//        try {
//            // Get counts for different statuses
//            List<IncidentResponseDTO> allIncidents = incidentService.getIncidents(
//                    null, null, null, null, null, userId);
//
//            long newCount = allIncidents.stream()
//                    .filter(i -> i.getStatus() == IncidentStatus.NEW)
//                    .count();
//
//            long assignedCount = allIncidents.stream()
//                    .filter(i -> i.getStatus() == IncidentStatus.ASSIGNED)
//                    .count();
//
//            long inProgressCount = allIncidents.stream()
//                    .filter(i -> i.getStatus() == IncidentStatus.IN_PROGRESS)
//                    .count();
//
//            long resolvedCount = allIncidents.stream()
//                    .filter(i -> i.getStatus() == IncidentStatus.RESOLVED)
//                    .count();
//
//            long closedCount = allIncidents.stream()
//                    .filter(i -> i.getStatus() == IncidentStatus.CLOSED)
//                    .count();
//
//            IncidentStatusSummaryDTO summary = new IncidentStatusSummaryDTO();
//            summary.setNewIncidents(newCount);
//            summary.setAssignedIncidents(assignedCount);
//            summary.setInProgressIncidents(inProgressCount);
//            summary.setResolvedIncidents(resolvedCount);
//            summary.setClosedIncidents(closedCount);
//            summary.setTotalIncidents(allIncidents.size());
//            summary.setTimestamp(LocalDateTime.now());
//
//            log.info("Returning status summary: {} total incidents", allIncidents.size());
//            return ResponseEntity.ok(summary);
//
//        } catch (Exception e) {
//            log.error("Error generating status summary: {}", e.getMessage(), e);
//            throw e;
//        }
//    }

    // ========== HELPER METHODS ==========

    private AuditLogDTO convertToAuditLogDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setActionType(auditLog.getActionType());
        dto.setActionDetails(auditLog.getActionDetails());
        dto.setTimestamp(auditLog.getTimestamp());
        dto.setIpAddress(auditLog.getIpAddress());
        dto.setUserAgent(auditLog.getUserAgent());

        if (auditLog.getEmployee() != null) {
            dto.setEmployeeName(auditLog.getEmployee().getFirstName() + " " +
                    auditLog.getEmployee().getLastName());
            dto.setEmployeeServiceNumber(auditLog.getEmployee().getServiceNumber());
        }

        if (auditLog.getIncident() != null) {
            dto.setIncidentId(auditLog.getIncident().getId());
            dto.setIncidentReportNumber(auditLog.getIncident().getReportNumber());
        }

        return dto;
    }

    // ========== DTO CLASSES ==========

    // Inner DTO classes for responses not defined elsewhere

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncidentUpdateDTO {
        private String description;
        private IncidentPriority priority;
        private List<Long> affectedResourceIds;
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JournalEntryCreateDTO {
        private String actionDescription;
        private String details;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationDTO {
        private Long id;
        private String type;
        private String message;
        private LocalDateTime timestamp;
        private boolean read;
        private Long incidentId;
        private String incidentReportNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncidentStatisticsDTO {
        private LocalDate date;
        private int totalIncidents;
        private int criticalIncidents;
        private int highPriorityIncidents;
        private int mediumPriorityIncidents;
        private int lowPriorityIncidents;
        private double averageResolutionTime; // minutes
        private int resolvedIncidents;
        private int closedIncidents;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStatisticsDTO {
        private int year;
        private int month;
        private Map<IncidentType, Integer> incidentsByType;
        private Map<IncidentPriority, Integer> incidentsByPriority;
        private double averageDailyIncidents;
        private double averageResolutionTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportDTO {
        private Long id;
        private String reportName;
        private LocalDateTime generatedAt;
        private String generatedBy;
        private String downloadUrl;
        private Map<String, Object> data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthStatusDTO {
        private String status;
        private LocalDateTime timestamp;
        private String service;
        private String version;
        private String database;
        private String externalServices;
        private String error;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncidentStatusSummaryDTO {
        private long newIncidents;
        private long assignedIncidents;
        private long inProgressIncidents;
        private long resolvedIncidents;
        private long closedIncidents;
        private long totalIncidents;
        private LocalDateTime timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditLogDTO {
        private Long id;
        private String actionType;
        private String actionDetails;
        private LocalDateTime timestamp;
        private String ipAddress;
        private String userAgent;
        private String employeeName;
        private String employeeServiceNumber;
        private Long incidentId;
        private String incidentReportNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JournalEntryDTO {
        private Long id;
        private LocalDateTime actionTime;
        private String actionDescription;
        private String details;
        private String employeeName;
        private String employeeServiceNumber;
    }
}
