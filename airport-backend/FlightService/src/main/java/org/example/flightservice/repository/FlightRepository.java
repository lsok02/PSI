package org.example.flightservice.repository;

import org.example.flightservice.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface FlightRepository extends JpaRepository<Flight, Long> {
}