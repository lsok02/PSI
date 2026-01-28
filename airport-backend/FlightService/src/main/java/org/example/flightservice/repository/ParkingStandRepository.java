package org.example.flightservice.repository;

import org.example.flightservice.model.entity.ParkingStand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ParkingStandRepository extends JpaRepository<ParkingStand, Long> {
}