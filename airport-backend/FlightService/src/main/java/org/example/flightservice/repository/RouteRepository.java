package org.example.flightservice.repository;

import org.example.flightservice.model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RouteRepository extends JpaRepository<Route, Long> {
}