package org.example.securityservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.ReportingSource;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentDTO {

    private Long id;

    @NotNull
    private String reportNumber;

    @NotNull
    private IncidentType type;

    @NotNull
    private IncidentPriority priority;

    @NotNull
    private Long locationId;

    @NotNull
    private String description;

    private Long dispatcherId;

    private Long assignedTeamId;

    private List<Long> affectedResourceIds;

    // Read-only fields
    private IncidentStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closureTime;

    private ReportingSource reportSource;
}