package org.example.flightservice.repository;

import org.example.flightservice.model.entity.Flight;
import org.example.flightservice.model.enumeration.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByScheduledDepartureTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT f FROM Flight f WHERE f.gate.terminal = :terminal " +
            "AND f.scheduledDepartureTime BETWEEN :start AND :end")
    List<Flight> findByGateTerminalAndScheduledDepartureTimeBetween(
            @Param("terminal") String terminal,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}