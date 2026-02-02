package org.example.flightservice.service;


import lombok.RequiredArgsConstructor;
import org.example.flightservice.model.entity.Flight;
import org.example.flightservice.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<Flight> findAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public List<Flight> findFlightsByDateRange(LocalDateTime start, LocalDateTime end) {
        return flightRepository.findByScheduledDepartureTimeBetween(start, end);
    }

    public List<Flight> findFlightsByTerminalAndDateRange(String terminal,
                                                          LocalDateTime start,
                                                          LocalDateTime end) {
        return flightRepository.findByGateTerminalAndScheduledDepartureTimeBetween(
                terminal, start, end);
    }
}