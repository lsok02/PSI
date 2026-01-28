package org.example.flightservice.repository;

import org.example.flightservice.model.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface GateRepository extends JpaRepository<Gate, Long> {
}