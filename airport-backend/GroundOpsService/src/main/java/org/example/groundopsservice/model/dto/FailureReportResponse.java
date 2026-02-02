package org.example.groundopsservice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FailureReportResponse {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private String failureType;
    private String description;
    private String urgency;
    private String location;
    private String status;
    private LocalDateTime reportedAt;
    private String reportedBy;
    private Long securityIncidentId;
}
