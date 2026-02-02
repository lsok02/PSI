package org.example.flightservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.flightservice.model.dto.FlightDTO;
import org.example.flightservice.model.entity.Flight;
import org.example.flightservice.model.enumeration.FlightStatus;
import org.example.flightservice.model.mapper.FlightMapper;
import org.example.flightservice.model.request.LockDelayRequest;
import org.example.flightservice.model.request.StatusUpdateRequest;
import org.example.flightservice.model.request.UnlockRequest;
import org.example.flightservice.service.FlightEnrichmentService;
import org.example.flightservice.service.FlightLockService;
import org.example.flightservice.service.FlightService;
import org.example.flightservice.service.FlightStatusService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;
    private final FlightMapper flightMapper;
    private final FlightLockService flightLockService;
    private final FlightStatusService flightStatusService;
    private final FlightEnrichmentService flightEnrichmentService;

    @GetMapping
    public ResponseEntity<List<FlightDTO>> getAllFlights(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<Flight> flights;

        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            flights = flightService.findFlightsByDateRange(startOfDay, endOfDay);
        } else {
            flights = flightService.findAllFlights();
        }

        List<FlightDTO> flightDTOs = flights.stream()
                .map(flight -> {
                    FlightDTO dto = flightMapper.toDto(flight);
                    flightEnrichmentService.enrichWithStatusManagement(flight, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(flightDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));

        FlightDTO dto = flightMapper.toDto(flight);
        flightEnrichmentService.enrichWithStatusManagement(flight, dto);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/lock-and-delay")
    public ResponseEntity<Map<String, Object>> lockAndDelayFlights(
            @RequestBody LockDelayRequest request) {

        LocalDateTime startOfDay = request.getDate().atStartOfDay();
        LocalDateTime endOfDay = request.getDate().atTime(23, 59, 59);

        List<Flight> flights = flightService.findFlightsByTerminalAndDateRange(
                        request.getTerminal(),
                        startOfDay,
                        endOfDay
                ).stream()
                .filter(flight -> !flightStatusService.hasDeparted(flight.getStatus()))
                .collect(Collectors.toList());

        List<Long> updatedFlightIds = flights.stream()
                .map(flight -> {
                    if (!flightLockService.isFlightLocked(flight.getId())) {
                        flight.setStatus(FlightStatus.DELAYED);
                        flight.setDelayReason("Automatic delay due to terminal issues");
                        flightService.saveFlight(flight);
                        return flight.getId();
                    }
                    return null;
                })
                .filter(id -> id != null)
                .collect(Collectors.toList());

        flightLockService.lockFlights(request.getTerminal(), request.getDate(), updatedFlightIds);

        return ResponseEntity.ok(Map.of(
                "message", "Flights locked and delayed successfully",
                "terminal", request.getTerminal(),
                "date", request.getDate(),
                "lockedFlightCount", updatedFlightIds.size(),
                "lockedFlightIds", updatedFlightIds
        ));
    }

    @PostMapping("/unlock")
    public ResponseEntity<Map<String, Object>> unlockFlights(
            @RequestBody UnlockRequest request) {

        List<Long> unlockedIds = flightLockService.unlockFlights(request.getTerminal(), request.getDate());

        if (unlockedIds != null) {
            return ResponseEntity.ok(Map.of(
                    "message", "Flights unlocked successfully",
                    "terminal", request.getTerminal(),
                    "date", request.getDate(),
                    "unlockedFlightCount", unlockedIds.size(),
                    "unlockedFlightIds", unlockedIds));
        }

        return ResponseEntity.ok(Map.of(
                "message", "No locked flights found for this terminal and date",
                "terminal", request.getTerminal(),
                "date", request.getDate(),
                "unlockedFlightCount", 0));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateFlightStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        Flight flight = flightService.findFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));

        FlightDTO currentDto = flightMapper.toDto(flight);
        flightEnrichmentService.enrichWithStatusManagement(flight, currentDto);

        if (currentDto.getIsLockedForStatusChange()) {
            if (currentDto.getCanBeCancelledOnly()) {
                if (request.getStatus() != FlightStatus.CANCELLED) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "error", "Flight is locked. Only CANCELLED status is allowed.",
                            "allowedStatus", FlightStatus.CANCELLED.name()));
                }
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Flight is completely locked for status changes.",
                        "allowedNextStatuses", currentDto.getAllowedNextStatuses()));
            }
        }

        if (!currentDto.getAllowedNextStatuses().isEmpty() &&
                !currentDto.getAllowedNextStatuses().contains(request.getStatus().name())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Requested status is not allowed.",
                    "allowedNextStatuses", currentDto.getAllowedNextStatuses(),
                    "requestedStatus", request.getStatus().name()));
        }

        flight.setStatus(request.getStatus());
        Flight updatedFlight = flightService.saveFlight(flight);
        FlightDTO updatedDto = flightMapper.toDto(updatedFlight);
        flightEnrichmentService.enrichWithStatusManagement(updatedFlight, updatedDto);

        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}/available-statuses")
    public ResponseEntity<Map<String, Object>> getAvailableStatuses(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));

        FlightDTO dto = flightMapper.toDto(flight);
        flightEnrichmentService.enrichWithStatusManagement(flight, dto);

        return ResponseEntity.ok(Map.of(
                "flightId", id,
                "flightNumber", flight.getFlightNumber(),
                "currentStatus", flight.getStatus().name(),
                "isLockedForStatusChange", dto.getIsLockedForStatusChange(),
                "canBeCancelledOnly", dto.getCanBeCancelledOnly(),
                "allowedNextStatuses", dto.getAllowedNextStatuses(),
                "allPossibleStatuses", flightStatusService.getAllPossibleStatuses(flight.getStatus())));
    }
}