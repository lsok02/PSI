package org.example.securityservice.repository;

import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatus(IncidentStatus status);

    List<Incident> findByPriority(IncidentPriority priority);

    List<Incident> findByType(IncidentType type);

    @Query("SELECT i FROM Incident i WHERE i.reportTime BETWEEN :startDate AND :endDate")
    List<Incident> findIncidentsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT i FROM Incident i WHERE i.status IN :statuses ORDER BY i.reportTime DESC")
    List<Incident> findActiveIncidents(@Param("statuses") List<IncidentStatus> statuses);

    boolean existsByReportNumber(String reportNumber);
}