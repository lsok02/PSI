package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.exception.IncidentNotFoundException;
import org.example.securityservice.model.dto.StatusChangeDTO;
import org.example.securityservice.model.entity.AuditLog;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.entity.LogEntry;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.repository.AuditLogRepository;
import org.example.securityservice.repository.IncidentRepository;
import org.example.securityservice.repository.LogEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentNotificationService {

    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final LogEntryRepository logEntryRepository;
    private final IncidentRepository incidentRepository;
    private final AuditLogRepository auditLogRepository;

    public void createInitialLog(Incident incident, Employee user, String description) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .performedBy(user)
                .actionTime(LocalDateTime.now())
                .actionDescription("Incident created")
                .build();
        logEntryRepository.save(logEntry);
    }

    public void createTeamAssignmentLog(Incident incident, Employee user, IncidentTeam team) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .performedBy(user)
                .actionTime(LocalDateTime.now())
                .actionDescription(String.format("Team %s assigned to incident", team.getTeamName()))
                .build();
        logEntryRepository.save(logEntry);
    }

    public void createStatusChangeLog(Incident incident, Employee user, IncidentStatus oldStatus,
            StatusChangeDTO statusChangeDTO) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .performedBy(user)
                .actionTime(LocalDateTime.now())
                .actionDescription(
                        String.format("Status changed from %s to %s", oldStatus, statusChangeDTO.getNewStatus()))
                .build();
        logEntryRepository.save(logEntry);
    }

    public void createUpdateLog(Incident incident, Employee user) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .performedBy(user)
                .actionTime(LocalDateTime.now())
                .actionDescription("Incident updated")
                .build();
        logEntryRepository.save(logEntry);
    }

    public void logIncidentCreation(Incident incident, Employee user) {
        auditLogService.logIncidentCreation(incident, user);
    }

    public void logTeamAssignment(Incident incident, IncidentTeam team, Employee user) {
        auditLogService.logTeamAssignment(incident, team, user);
    }

    public void logStatusChange(Incident incident, IncidentStatus oldStatus, IncidentStatus newStatus, Employee user) {
        auditLogService.logStatusChange(incident, oldStatus, newStatus, user);
    }

    public void sendTeamAssignmentNotification(IncidentTeam team, Incident incident) {
        notificationService.sendTeamAssignmentNotification(team, incident);
    }

    public void sendStatusUpdateNotification(Incident incident) {
        notificationService.sendStatusUpdateNotification(incident);
    }

    public void sendManagerReviewRequest(Incident incident) {
        notificationService.sendManagerReviewRequest(incident);
    }

    public void sendEscalationNotification(Incident incident) {
        notificationService.sendEscalationNotification(incident);
    }

    public void checkForEscalation(Incident incident) {
        if (incident.getPriority() == IncidentPriority.HIGH ||
                incident.getPriority() == IncidentPriority.CRITICAL) {

            scheduleEscalationCheck(incident);
        }
    }

    private void scheduleEscalationCheck(Incident incident) {
        new Thread(() -> {
            try {
                Thread.sleep(10 * 60 * 1000); // 10 minutes

                Incident currentIncident = incidentRepository.findById(incident.getId())
                        .orElseThrow(() -> new IncidentNotFoundException(incident.getId()));

                if (currentIncident.getStatus() == IncidentStatus.ASSIGNED) {
                    createEscalationLog(currentIncident);
                    notificationService.sendEscalationNotification(currentIncident);
                    log.warn("Incident {} escalated due to lack of progress",
                            currentIncident.getReportNumber());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Error in escalation check: {}", e.getMessage());
            }
        }).start();
    }

    private void createEscalationLog(Incident incident) {
        LogEntry escalationLog = LogEntry.builder()
                .incident(incident)
                .actionTime(LocalDateTime.now())
                .actionDescription("Incident escalated")
                .build();
        logEntryRepository.save(escalationLog);
    }

    public void createEscalationLog(Incident incident, Employee user, IncidentPriority oldPriority,
            IncidentPriority newPriority) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .performedBy(user)
                .actionTime(LocalDateTime.now())
                .actionDescription(
                        String.format("User manually escalated priority from %s to %s", oldPriority, newPriority))
                .build();
        logEntryRepository.save(logEntry);
    }

    public void logSensorIncidentCreation(Incident incident) {
        AuditLog log = AuditLog.builder()
                .incident(incident)
                .actionType("SENSOR_INCIDENT_CREATED")
                .actionDetails(String.format("Incident %s created from sensor event", incident.getReportNumber()))
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

}