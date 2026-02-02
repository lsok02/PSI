package org.example.flightservice.service;

import lombok.RequiredArgsConstructor;
import org.example.flightservice.model.dto.FlightDTO;
import org.example.flightservice.model.entity.Flight;
import org.example.flightservice.model.enumeration.FlightStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FlightEnrichmentService {

    private final FlightLockService flightLockService;
    private final FlightStatusService flightStatusService;

    public void enrichWithStatusManagement(Flight flight, FlightDTO dto) {
        boolean isLocked = flightLockService.isFlightLocked(flight.getId());

        if (isLocked) {
            dto.setIsLockedForStatusChange(true);
            dto.setCanBeCancelledOnly(true);
            dto.setAllowedNextStatuses(Collections.singletonList(FlightStatus.CANCELLED.name()));
        } else {
            dto.setIsLockedForStatusChange(false);
            dto.setCanBeCancelledOnly(false);
            dto.setAllowedNextStatuses(flightStatusService.getAllPossibleStatuses(flight.getStatus()));
        }
    }
}