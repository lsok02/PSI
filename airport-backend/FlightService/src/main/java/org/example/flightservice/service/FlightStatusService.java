package org.example.flightservice.service;

import org.example.flightservice.model.enumeration.FlightStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FlightStatusService {

    public List<String> getAllPossibleStatuses(FlightStatus currentStatus) {
        return switch (currentStatus) {
            case PLANNED -> Arrays.asList(
                    FlightStatus.DELAYED.name(),
                    FlightStatus.GATE_CLOSED.name(),
                    FlightStatus.BOARDING.name(),
                    FlightStatus.CANCELLED.name(),
                    FlightStatus.DEPARTED.name());
            case DELAYED -> Arrays.asList(
                    FlightStatus.PLANNED.name(),
                    FlightStatus.GATE_CLOSED.name(),
                    FlightStatus.BOARDING.name(),
                    FlightStatus.CANCELLED.name(),
                    FlightStatus.DEPARTED.name());
            case GATE_CLOSED -> Arrays.asList(
                    FlightStatus.BOARDING.name(),
                    FlightStatus.DELAYED.name(),
                    FlightStatus.CANCELLED.name());
            case BOARDING -> Arrays.asList(
                    FlightStatus.DEPARTED.name(),
                    FlightStatus.DELAYED.name(),
                    FlightStatus.CANCELLED.name());
            default -> Collections.emptyList();
        };
    }

    public boolean hasDeparted(FlightStatus status) {
        return status == FlightStatus.DEPARTED ||
                status == FlightStatus.LANDED;
    }
}