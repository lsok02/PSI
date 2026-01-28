package org.example.flightservice.repository;

import org.example.flightservice.model.entity.Runway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RunwayRepository extends JpaRepository<Runway, Long> {
}