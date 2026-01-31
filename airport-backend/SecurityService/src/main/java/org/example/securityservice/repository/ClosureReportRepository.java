package org.example.securityservice.repository;

import org.example.securityservice.model.entity.ClosureReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClosureReportRepository extends JpaRepository<ClosureReport, Long> {

    // Znajdź raport zamknięcia po ID incydentu
    Optional<ClosureReport> findByIncidentId(Long incidentId);

    // Sprawdź czy istnieje raport dla incydentu
    boolean existsByIncidentId(Long incidentId);

    // Znajdź raporty po statusie zatwierdzenia
    List<ClosureReport> findByApprovalStatus(String approvalStatus);

    // Znajdź raporty utworzone przez konkretnego pracownika
    List<ClosureReport> findByCreatedById(Long employeeId);

    // Znajdź raporty zatwierdzone przez konkretnego managera
    List<ClosureReport> findByApprovedById(Long managerId);
}