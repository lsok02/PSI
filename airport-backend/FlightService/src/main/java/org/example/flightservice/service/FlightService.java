package org.example.flightservice.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flightservice.model.dto.FlightDTO;
import org.example.flightservice.model.entity.Flight;
import org.example.flightservice.model.enumeration.FlightStatus;
import org.example.flightservice.model.mapper.FlightMapper;
import org.example.flightservice.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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