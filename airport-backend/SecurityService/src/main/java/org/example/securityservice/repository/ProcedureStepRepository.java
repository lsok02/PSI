package org.example.securityservice.repository;

import org.example.securityservice.model.entity.ProcedureStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureStepRepository extends JpaRepository<ProcedureStep, Long> {
}