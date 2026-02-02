package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.StatusChangeDTO;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.entity.LogEntry;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.repository.LogEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentNotificationService {

    private final LogEntryRepository logEntryRepository;

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
}