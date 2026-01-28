package org.example.flightservice.repository;

import org.example.flightservice.model.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}