package org.example.securityservice.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private String flightNumber;
    private String airlineCode;
    private String airlineName;
    private String aircraftType;
    private String originAirport;
    private String destinationAirport;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private LocalDateTime estimatedDeparture;
    private LocalDateTime estimatedArrival;
    private FlightStatus status; // DELAYED, ON_TIME, CANCELLED, DIVERTED
    private String gate;
    private String terminal;
    private String parkingStand;

    // Informacje o pasażerach
    private Integer passengerCount;
    private Integer crewCount;

    // Operacyjne
    private String currentZone;
    private boolean isOnGround;

    // Opcjonalnie: dane kontaktowe
    private String pilotInCommand;
    private String contactFrequency;

    public enum FlightStatus {
        SCHEDULED,
        ON_TIME,
        DELAYED,
        CANCELLED,
        DIVERTED,
        IN_FLIGHT,
        LANDED,
        DEPARTED
    }
}