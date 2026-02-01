package org.example.flightservice.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.flightservice.model.dto.FlightDTO;
import org.example.flightservice.model.entity.Flight;
import org.example.flightservice.model.enumeration.FlightStatus;
import org.example.flightservice.model.mapper.FlightMapper;
import org.example.flightservice.service.FlightService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;
    private final FlightMapper flightMapper;

    // Cache dla zablokowanych lotów (terminal, data -> lista lotów)
    private final Map<String, List<Long>> lockedFlightsCache = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Endpoint 1: Pobierz wszystkie loty z opcjonalnym filtrem daty
     * GET /api/flights?date=2024-01-15
     */
    @GetMapping
    public ResponseEntity<List<FlightDTO>> getAllFlights(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<Flight> flights;

        if (date != null) {
            // Pobierz loty dla konkretnego dnia
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            flights = flightService.findFlightsByDateRange(startOfDay, endOfDay);
        } else {
            // Pobierz wszystkie loty
            flights = flightService.findAllFlights();
        }

        // Mapowanie do DTO z dodatkowymi polami status management
        List<FlightDTO> flightDTOs = flights.stream()
                .map(flight -> {
                    FlightDTO dto = flightMapper.toDto(flight);
                    enrichWithStatusManagement(flight, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(flightDTOs);
    }

    /**
     * Endpoint 2: Zablokuj i zaktualizuj loty na DELAYED dla danego terminala i dnia
     * POST /api/flights/lock-and-delay
     * Request body: {"date": "2024-01-15", "terminal": "A"}
     */
    @PostMapping("/lock-and-delay")
    public ResponseEntity<Map<String, Object>> lockAndDelayFlights(
            @RequestBody LockDelayRequest request) {

        LocalDateTime startOfDay = request.getDate().atStartOfDay();
        LocalDateTime endOfDay = request.getDate().atTime(23, 59, 59);

        // Znajdź loty które jeszcze nie odleciały dla danego terminala i dnia
        List<Flight> flights = flightService.findFlightsByTerminalAndDateRange(
                        request.getTerminal(),
                        startOfDay,
                        endOfDay
                ).stream()
                .filter(flight -> !hasDeparted(flight.getStatus()))
                .collect(Collectors.toList());

        // Zaktualizuj status na DELAYED i zablokuj
        List<Long> updatedFlightIds = flights.stream()
                .map(flight -> {
                    // Sprawdź czy lot nie jest już zablokowany
                    if (!isFlightLocked(flight.getId())) {
                        flight.setStatus(FlightStatus.DELAYED);
                        flight.setDelayReason("Automatic delay due to terminal issues");
                        flightService.saveFlight(flight);
                        return flight.getId();
                    }
                    return null;
                })
                .filter(id -> id != null)
                .collect(Collectors.toList());

        // Zablokuj loty w cache
        String cacheKey = generateCacheKey(request.getTerminal(), request.getDate());
        lockedFlightsCache.put(cacheKey, updatedFlightIds);

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

        String cacheKey = generateCacheKey(request.getTerminal(), request.getDate());

        if (lockedFlightsCache.containsKey(cacheKey)) {
            List<Long> unlockedIds = lockedFlightsCache.remove(cacheKey);

            return ResponseEntity.ok(Map.of(
                    "message", "Flights unlocked successfully",
                    "terminal", request.getTerminal(),
                    "date", request.getDate(),
                    "unlockedFlightCount", unlockedIds.size(),
                    "unlockedFlightIds", unlockedIds
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "No locked flights found for this terminal and date",
                "terminal", request.getTerminal(),
                "date", request.getDate(),
                "unlockedFlightCount", 0
        ));
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateFlightStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        Flight flight = flightService.findFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));

        FlightDTO currentDto = flightMapper.toDto(flight);
        enrichWithStatusManagement(flight, currentDto);

        // Walidacja: czy lot jest zablokowany do zmiany statusu
        if (currentDto.getIsLockedForStatusChange()) {
            if (currentDto.getCanBeCancelledOnly()) {
                // Można zmienić tylko na CANCELLED
                if (request.getStatus() != FlightStatus.CANCELLED) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "error", "Flight is locked. Only CANCELLED status is allowed.",
                            "allowedStatus", FlightStatus.CANCELLED.name()
                    ));
                }
            } else {
                // Całkowicie zablokowany
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Flight is completely locked for status changes.",
                        "allowedNextStatuses", currentDto.getAllowedNextStatuses()
                ));
            }
        }

        // Walidacja: czy żądany status jest dozwolony
        if (!currentDto.getAllowedNextStatuses().isEmpty() &&
                !currentDto.getAllowedNextStatuses().contains(request.getStatus().name())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Requested status is not allowed.",
                    "allowedNextStatuses", currentDto.getAllowedNextStatuses(),
                    "requestedStatus", request.getStatus().name()
            ));
        }

        // Aktualizacja statusu
        flight.setStatus(request.getStatus());
        Flight updatedFlight = flightService.saveFlight(flight);
        FlightDTO updatedDto = flightMapper.toDto(updatedFlight);
        enrichWithStatusManagement(updatedFlight, updatedDto);

        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}/available-statuses")
    public ResponseEntity<Map<String, Object>> getAvailableStatuses(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));

        FlightDTO dto = flightMapper.toDto(flight);
        enrichWithStatusManagement(flight, dto);

        return ResponseEntity.ok(Map.of(
                "flightId", id,
                "flightNumber", flight.getFlightNumber(),
                "currentStatus", flight.getStatus().name(),
                "isLockedForStatusChange", dto.getIsLockedForStatusChange(),
                "canBeCancelledOnly", dto.getCanBeCancelledOnly(),
                "allowedNextStatuses", dto.getAllowedNextStatuses(),
                "allPossibleStatuses", getAllPossibleStatuses(flight.getStatus())
        ));
    }

    // ========== PRYWATNE METODY POMOCNICZE ==========

    private void enrichWithStatusManagement(Flight flight, FlightDTO dto) {
        // Sprawdź czy lot jest zablokowany
        boolean isLocked = isFlightLocked(flight.getId());

        if (isLocked) {
            dto.setIsLockedForStatusChange(true);
            dto.setCanBeCancelledOnly(true);
            dto.setAllowedNextStatuses(Collections.singletonList(FlightStatus.CANCELLED.name()));
        } else {
            dto.setIsLockedForStatusChange(false);
            dto.setCanBeCancelledOnly(false);
            dto.setAllowedNextStatuses(getAllPossibleStatuses(flight.getStatus()));
        }
    }

    private boolean isFlightLocked(Long flightId) {
        return lockedFlightsCache.values().stream()
                .anyMatch(flightIds -> flightIds.contains(flightId));
    }

    private boolean hasDeparted(FlightStatus status) {
        return status == FlightStatus.DEPARTED ||
                status == FlightStatus.LANDED;
    }

    private List<String> getAllPossibleStatuses(FlightStatus currentStatus) {
        return switch (currentStatus) {
            case PLANNED -> Arrays.asList(
                    FlightStatus.DELAYED.name(),
                    FlightStatus.GATE_CLOSED.name(),
                    FlightStatus.BOARDING.name(),
                    FlightStatus.CANCELLED.name(),
                    FlightStatus.DEPARTED.name()
            );
            case DELAYED -> Arrays.asList(
                    FlightStatus.PLANNED.name(),
                    FlightStatus.GATE_CLOSED.name(),
                    FlightStatus.BOARDING.name(),
                    FlightStatus.CANCELLED.name(),
                    FlightStatus.DEPARTED.name()
            );
            case GATE_CLOSED -> Arrays.asList(
                    FlightStatus.BOARDING.name(),
                    FlightStatus.DELAYED.name(),
                    FlightStatus.CANCELLED.name()
            );
            case BOARDING -> Arrays.asList(
                    FlightStatus.DEPARTED.name(),
                    FlightStatus.DELAYED.name(),
                    FlightStatus.CANCELLED.name()
            );
            default -> Collections.emptyList(); // Dla DEPARTED, LANDED, CANCELLED
        };
    }

    private String generateCacheKey(String terminal, LocalDate date) {
        return terminal + "_" + date.toString();
    }

    // ========== KLASY REQUEST ==========

    @Data
    public static class LockDelayRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate date;
        private String terminal;
    }

    @Data
    public static class UnlockRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate date;
        private String terminal;
    }

    @Data
    public static class StatusUpdateRequest {
        private FlightStatus status;
    }
}