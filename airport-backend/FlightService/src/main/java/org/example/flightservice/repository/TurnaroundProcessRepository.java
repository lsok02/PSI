package org.example.flightservice.repository;

import org.example.flightservice.model.entity.TurnaroundProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TurnaroundProcessRepository extends JpaRepository<TurnaroundProcess, Long> {
}