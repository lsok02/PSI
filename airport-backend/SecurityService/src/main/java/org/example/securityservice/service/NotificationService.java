package org.example.securityservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.entity.AuditLog;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.repository.AuditLogRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;


    public void sendTeamAssignmentNotification(IncidentTeam team, Incident incident) {
        // WebSocket notification
        messagingTemplate.convertAndSend(
                "/topic/team-notifications/" + team.getId(),
                new TeamAssignmentNotification(incident)
        );

        // Email notification (mock for demo)
        log.info("Sending team assignment email for incident: {}", incident.getReportNumber());

        // For demo purposes, we'll just log
        team.getMembers().forEach(member -> {
            log.info("Notification sent to team member {}: {}",
                    member.getId(), incident.getReportNumber());
        });
    }

    public void sendCriticalAlert(Incident incident) {
        messagingTemplate.convertAndSend(
                "/topic/critical-alerts",
                new CriticalIncidentNotification(incident)
        );

        // Send SMS/email to on-call managers (simulated)
        log.warn("CRITICAL ALERT: {}", incident.getReportNumber());
    }

    public void sendStatusUpdateNotification(Incident incident) {
        messagingTemplate.convertAndSend(
                "/topic/incident-updates/" + incident.getId(),
                new IncidentStatusUpdate(incident)
        );
    }

    public void sendManagerReviewRequest(Incident incident) {
        log.info("Manager review requested for incident: {}", incident.getReportNumber());
    }

    public void sendEscalationNotification(Incident incident) {
        log.warn("ESCALATION: Incident {} requires manager attention", incident.getReportNumber());
    }

    // Inner classes for notification messages
    @Data
    @AllArgsConstructor
    private static class TeamAssignmentNotification {
        private Long incidentId;
        private String reportNumber;
        private String priority;
        private String location;

        public TeamAssignmentNotification(Incident incident) {
            this.incidentId = incident.getId();
            this.reportNumber = incident.getReportNumber();
            this.priority = incident.getPriority().name();
            this.location = incident.getLocation().getName();
        }
    }

    @Data
    @AllArgsConstructor
    private static class CriticalIncidentNotification {
        private Long incidentId;
        private String reportNumber;
        private String type;
        private String location;

        // Dodaj konstruktor dla Incident
        public CriticalIncidentNotification(Incident incident) {
            this.incidentId = incident.getId();
            this.reportNumber = incident.getReportNumber();
            this.type = incident.getType().name();
            this.location = incident.getLocation().getName();
        }
    }

    @Data
    @AllArgsConstructor
    public static class IncidentStatusUpdate {
        private Long incidentId;
        private String newStatus;
        private String timestamp;

        // Konstruktor dla Incident - TEGO BRAKOWAŁO
        public IncidentStatusUpdate(Incident incident) {
            this.incidentId = incident.getId();
            this.newStatus = incident.getStatus().name();
            this.timestamp = LocalDateTime.now().toString();
        }
    }

}