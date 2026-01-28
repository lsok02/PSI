package org.example.flightservice.repository;

import org.example.flightservice.model.entity.GroundHandling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface GroundHandlingRepository extends JpaRepository<GroundHandling, Long> {
}