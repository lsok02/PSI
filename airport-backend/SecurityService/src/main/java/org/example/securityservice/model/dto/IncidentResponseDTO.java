package org.example.securityservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.ReportingSource;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentResponseDTO {
    private Long id;
    private String reportNumber;
    private IncidentType type;
    private IncidentPriority priority;
    private IncidentStatus status;
    private ReportingSource reportSource;
    private String description;
    private LocalDateTime creationTime;
    private LocalDateTime closureTime;

    // Relacje - używamy konkretnych typów DTO
    private LocationDTO location;
    private DispatcherDTO registeredBy; // Teraz mamy konkretny typ
    private IncidentTeamDTO assignedTeam;
    private List<AirportResourceDTO> affectedResources;
    private List<LogEntryDTO> journalEntries;
    private StandardOperatingProcedureDTO procedure;
    private ClosureReportDTO closureReport;

    // Business flags - zależne od roli użytkownika
    private boolean canEdit;
    private boolean canAssignTeam;
    private boolean canChangeStatus;
    private boolean canAddClosureReport;
    private boolean canClose;
    private List<IncidentStatus> allowedStatusTransitions;

    // Info o użytkowniku (dla UI)
    private String userRole; // "DISPATCHER", "SECURITY_MANAGER", "TEAM_MEMBER"
    private boolean isAssignedTeamMember;
}