package org.example.groundopsservice.repository;

import org.example.groundopsservice.model.entity.FailureReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailureReportRepository extends JpaRepository<FailureReport, Long> {}
