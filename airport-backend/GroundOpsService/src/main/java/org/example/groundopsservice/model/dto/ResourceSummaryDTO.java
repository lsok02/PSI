package org.example.groundopsservice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResourceSummaryDTO {
    private Long id;
    private String name;
    private String status;
    private String resourceType;
    private String details;
    private LocalDateTime nextMaintenanceDate;
}
