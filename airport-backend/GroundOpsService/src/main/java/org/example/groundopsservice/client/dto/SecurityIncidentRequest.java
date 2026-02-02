package org.example.groundopsservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityIncidentRequest {
    private String type;
    private String priority;
    private Long locationId;
    private String description;
    private String reportSource;
}
