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

    private LocationDTO location;
    private DispatcherDTO registeredBy; // Teraz mamy konkretny typ

    private IncidentTeamDTO assignedTeam;
    private List<AirportResourceDTO> affectedResources;
    private List<LogEntryDTO> journalEntries;
    private StandardOperatingProcedureDTO procedure;


}