package org.example.securityservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.entity.SensorEvent;
import org.example.securityservice.model.enumeration.SensorType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorEventDTO {
    private Long id;
    private String sensorId;
    private SensorType sensorType;
    private String locationDetails;
    private LocalDateTime timestamp;
    private Long locationId;
    private String locationName;
    private Long incidentId;
    private Boolean isProcessed;

    public static SensorEventDTO fromEntity(SensorEvent event) {
        SensorEventDTO dto = SensorEventDTO.builder()
                .id(event.getId())
                .sensorId(event.getSensorId())
                .sensorType(event.getSensorType())
                .locationDetails(event.getLocationDetails())
                .timestamp(event.getTimestamp())
                .isProcessed(event.getIsProcessed())
                .build();

        if (event.getLocation() != null) {
            dto.setLocationId(event.getLocation().getId());
            dto.setLocationName(event.getLocation().getName());
        }

        if (event.getIncident() != null) {
            dto.setIncidentId(event.getIncident().getId());
        }

        return dto;
    }
}