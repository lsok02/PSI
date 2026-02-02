package org.example.groundopsservice.model.dto;

import lombok.Data;
import org.example.groundopsservice.model.enumeration.FailureType;
import org.example.groundopsservice.model.enumeration.FailureUrgency;

@Data
public class FailureReportRequest {
    private Long resourceId;
    private FailureType failureType;
    private String description;
    private FailureUrgency urgency;
    private String location;
    private Long securityLocationId;
}
