package org.example.flightservice.model.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.flightservice.model.dto.FlightDTO;
import org.example.flightservice.model.entity.Aircraft;
import org.example.flightservice.model.entity.Flight;
import org.example.flightservice.model.entity.Gate;
import org.example.flightservice.model.entity.Route;
import org.example.flightservice.model.entity.Runway;
import org.example.flightservice.model.enumeration.FlightStatus;
import org.example.flightservice.repository.AircraftRepository;
import org.example.flightservice.repository.GateRepository;
import org.example.flightservice.repository.RouteRepository;
import org.example.flightservice.repository.RunwayRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FlightMapper {

    private final GateRepository gateRepository;
    private final AircraftRepository aircraftRepository;
    private final RunwayRepository runwayRepository;
    private final RouteRepository routeRepository;

    /**
     * Mapowanie encji Flight na FlightDTO
     */
    public FlightDTO toDto(Flight flight) {
        if (flight == null) return null;

        FlightDTO.FlightDTOBuilder builder = FlightDTO.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .scheduledDepartureTime(flight.getScheduledDepartureTime())
                .actualDepartureTime(flight.getActualDepartureTime())
                .scheduledArrivalTime(flight.getScheduledArrivalTime())
                .actualArrivalTime(flight.getActualArrivalTime())
                .status(flight.getStatus().name())
                .estimatedDelayMinutes(flight.getEstimatedDelay())
                .delayReason(flight.getDelayReason())

                // Domyślne wartości dla pól status management
                .isLockedForStatusChange(false)
                .canBeCancelledOnly(false)
                .allowedNextStatuses(Collections.emptyList());

        // Mapowanie Gate
        if (flight.getGate() != null) {
            builder.gateId(flight.getGate().getId())
                    .gateNumber(flight.getGate().getGateNumber())
                    .terminal(flight.getGate().getTerminal())
                    .gateAvailable(flight.getGate().getIsAvailable());
        }

        // Mapowanie Aircraft
        if (flight.getAircraft() != null) {
            builder.aircraftId(flight.getAircraft().getId())
                    .aircraftRegistration(flight.getAircraft().getRegistrationNumber())
                    .aircraftType(flight.getAircraft().getModel());
        }

        // Mapowanie Runway
        if (flight.getRunway() != null) {
            builder.runwayId(flight.getRunway().getId())
                    .runwayName(flight.getRunway().getRunwayNumber());
        }

        // Mapowanie Route i Airport
        if (flight.getRoute() != null) {
            builder.routeId(flight.getRoute().getId());

            if (flight.getRoute().getDepartureAirport() != null) {
                builder.departureAirport(flight.getRoute().getDepartureAirport());
            }

            if (flight.getRoute().getDestinationAirport() != null) {
                builder.arrivalAirport(flight.getRoute().getDestinationAirport());
            }
        }

        return builder.build();
    }

    /**
     * Mapowanie FlightDTO na encję Flight
     */
    public Flight toEntity(FlightDTO dto) {
        if (dto == null) return null;

        Flight flight = new Flight();
        flight.setId(dto.getId());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setScheduledDepartureTime(dto.getScheduledDepartureTime());
        flight.setActualDepartureTime(dto.getActualDepartureTime());
        flight.setScheduledArrivalTime(dto.getScheduledArrivalTime());
        flight.setActualArrivalTime(dto.getActualArrivalTime());
        flight.setStatus(FlightStatus.valueOf(dto.getStatus()));
        flight.setEstimatedDelay(dto.getEstimatedDelayMinutes());
        flight.setDelayReason(dto.getDelayReason());

        // Mapowanie relacji na podstawie ID z DTO

        if (dto.getGateId() != null) {
            Gate gate = gateRepository.findById(dto.getGateId())
                    .orElseThrow(() -> new EntityNotFoundException("Gate not found with id: " + dto.getGateId()));
            flight.setGate(gate);
        }

        if (dto.getAircraftId() != null) {
            Aircraft aircraft = aircraftRepository.findById(dto.getAircraftId())
                    .orElseThrow(() -> new EntityNotFoundException("Aircraft not found with id: " + dto.getAircraftId()));
            flight.setAircraft(aircraft);
        }

        if (dto.getRunwayId() != null) {
            Runway runway = runwayRepository.findById(dto.getRunwayId())
                    .orElseThrow(() -> new EntityNotFoundException("Runway not found with id: " + dto.getRunwayId()));
            flight.setRunway(runway);
        }

        if (dto.getRouteId() != null) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + dto.getRouteId()));
            flight.setRoute(route);
        }

        return flight;
    }

    /**
     * Mapowanie kolekcji Flight na FlightDTO
     */
    public List<FlightDTO> toDtoList(List<Flight> flights) {
        if (flights == null) return Collections.emptyList();

        return flights.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    /**
     * Mapowanie FlightDTO na encję Flight (update istniejącej)
     */
    public Flight updateEntityFromDto(FlightDTO dto, Flight existingFlight) {
        if (dto == null || existingFlight == null) return existingFlight;

        // Aktualizuj podstawowe pola
        if (dto.getFlightNumber() != null) {
            existingFlight.setFlightNumber(dto.getFlightNumber());
        }

        if (dto.getScheduledDepartureTime() != null) {
            existingFlight.setScheduledDepartureTime(dto.getScheduledDepartureTime());
        }

        if (dto.getScheduledArrivalTime() != null) {
            existingFlight.setScheduledArrivalTime(dto.getScheduledArrivalTime());
        }

        if (dto.getStatus() != null) {
            existingFlight.setStatus(FlightStatus.valueOf(dto.getStatus()));
        }

        if (dto.getEstimatedDelayMinutes() != null) {
            existingFlight.setEstimatedDelay(dto.getEstimatedDelayMinutes());
        }

        if (dto.getDelayReason() != null) {
            existingFlight.setDelayReason(dto.getDelayReason());
        }

        // Aktualizuj relacje na podstawie ID z DTO

        if (dto.getGateId() != null &&
                (existingFlight.getGate() == null ||
                        !existingFlight.getGate().getId().equals(dto.getGateId()))) {

            Gate gate = gateRepository.findById(dto.getGateId())
                    .orElseThrow(() -> new EntityNotFoundException("Gate not found with id: " + dto.getGateId()));
            existingFlight.setGate(gate);
        }

        if (dto.getAircraftId() != null &&
                (existingFlight.getAircraft() == null ||
                        !existingFlight.getAircraft().getId().equals(dto.getAircraftId()))) {

            Aircraft aircraft = aircraftRepository.findById(dto.getAircraftId())
                    .orElseThrow(() -> new EntityNotFoundException("Aircraft not found with id: " + dto.getAircraftId()));
            existingFlight.setAircraft(aircraft);
        }

        if (dto.getRunwayId() != null &&
                (existingFlight.getRunway() == null ||
                        !existingFlight.getRunway().getId().equals(dto.getRunwayId()))) {

            Runway runway = runwayRepository.findById(dto.getRunwayId())
                    .orElseThrow(() -> new EntityNotFoundException("Runway not found with id: " + dto.getRunwayId()));
            existingFlight.setRunway(runway);
        }

        if (dto.getRouteId() != null &&
                (existingFlight.getRoute() == null ||
                        !existingFlight.getRoute().getId().equals(dto.getRouteId()))) {

            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + dto.getRouteId()));
            existingFlight.setRoute(route);
        }

        return existingFlight;
    }
}