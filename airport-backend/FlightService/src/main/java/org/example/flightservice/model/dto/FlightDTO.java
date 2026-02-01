package org.example.flightservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.flightservice.model.enumeration.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    private Long id;
    private String flightNumber;
    private LocalDateTime scheduledDepartureTime;
    private LocalDateTime actualDepartureTime;
    private LocalDateTime scheduledArrivalTime;
    private LocalDateTime actualArrivalTime;
    private String status;
    private Integer estimatedDelayMinutes;
    private String delayReason;

    // Dodatkowe pola status management
    private Boolean isLockedForStatusChange;
    private Boolean canBeCancelledOnly;
    private List<String> allowedNextStatuses;

    // Relacje
    private Long gateId;
    private String gateNumber;
    private String terminal;
    private Boolean gateAvailable;

    private Long aircraftId;
    private String aircraftRegistration;
    private String aircraftType;

    private Long runwayId;
    private String runwayName;

    private Long routeId;
    private String departureAirport;
    private String arrivalAirport;
}