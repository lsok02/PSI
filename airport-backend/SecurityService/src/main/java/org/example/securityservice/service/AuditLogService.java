package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.entity.AuditLog;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.repository.AuditLogRepository;
import org.example.securityservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logIncidentCreation(Incident incident, Employee dispatcher) {
        AuditLog log = AuditLog.builder()
                .incident(incident)
                .employee(dispatcher) // Używamy employee
                .actionType("INCIDENT_CREATED")
                .actionDetails(String.format("Incident %s created manually", incident.getReportNumber()))
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    public void logTeamAssignment(Incident incident, IncidentTeam team, Employee assignedBy) {
        AuditLog log = AuditLog.builder()
                .incident(incident)
                .employee(assignedBy) // Zmiana z userId na employee
                .actionType("TEAM_ASSIGNED")
                .actionDetails(String.format("Team %s assigned to incident", team.getTeamName()))
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    public void logStatusChange(Incident incident, IncidentStatus oldStatus,
                                IncidentStatus newStatus, Employee changedBy) {
        AuditLog log = AuditLog.builder()
                .incident(incident)
                .employee(changedBy) // Zmiana z userId na employee
                .actionType("STATUS_CHANGED")
                .actionDetails(String.format("Status changed from %s to %s", oldStatus, newStatus))
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    public void logClosureReport(Incident incident, Employee reportedBy) {
        AuditLog log = AuditLog.builder()
                .incident(incident)
                .employee(reportedBy) // Zmiana z userId na employee
                .actionType("CLOSURE_REPORT_ADDED")
                .actionDetails("Closure report submitted")
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    // Dodatkowa metoda jeśli potrzebujesz logować bez employee (np. systemowe akcje)
    public void logSystemAction(Incident incident, String actionType, String details) {
        AuditLog log = AuditLog.builder()
                .incident(incident)
                .actionType(actionType)
                .actionDetails(details)
                .timestamp(LocalDateTime.now())
                // employee pozostaje null dla akcji systemowych
                .build();
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAuditLogsByIncident(Long incidentId) {
        return auditLogRepository.findByIncidentId(incidentId);
    }
}