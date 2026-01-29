package org.example.groundopsservice.repository;

import org.example.groundopsservice.model.entity.GroundWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroundWorkerRepository extends JpaRepository<GroundWorker, Long> {}