package org.example.flightservice.model.request;

import lombok.Data;
import org.example.flightservice.model.enumeration.FlightStatus;

@Data
public class StatusUpdateRequest {
    private FlightStatus status;
}