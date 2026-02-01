package org.example.flightservice.repository;

import org.example.flightservice.model.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {
    Optional<Gate> findByGateNumberAndTerminal(String gateNumber, String terminal);
    List<Gate> findByTerminal(String terminal);
    List<Gate> findByIsAvailableTrue();
}