package org.example.securityservice.repository;

import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeamMember;
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

    List<Incident> findByStatusAndPriority(IncidentStatus status, IncidentPriority priority);

    @Query("SELECT i FROM Incident i WHERE i.reportTime BETWEEN :startDate AND :endDate ORDER BY i.reportTime DESC")
    List<Incident> findIncidentsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT i FROM Incident i WHERE i.status IN :statuses ORDER BY i.reportTime DESC")
    List<Incident> findActiveIncidents(@Param("statuses") List<IncidentStatus> statuses);

    @Query("SELECT i FROM Incident i WHERE i.status IN :statuses")
    List<Incident> findByStatusIn(@Param("statuses") List<IncidentStatus> statuses);

    boolean existsByReportNumber(String reportNumber);

    // Metoda do wyszukiwania z wieloma filtrami
    @Query("SELECT i FROM Incident i WHERE " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(:priority IS NULL OR i.priority = :priority) AND " +
            "(:type IS NULL OR i.type = :type) AND " +
            "(:from IS NULL OR i.reportTime >= :from) AND " +
            "(:to IS NULL OR i.reportTime <= :to) " +
            "ORDER BY i.reportTime DESC")
    List<Incident> findByFilters(
            @Param("status") IncidentStatus status,
            @Param("priority") IncidentPriority priority,
            @Param("type") IncidentType type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // Metoda do wyszukiwania incydentów przypisanych do członka zespołu
    @Query("SELECT DISTINCT i FROM Incident i " +
            "JOIN i.assignedTeam t " +
            "JOIN t.members m " +
            "WHERE m = :member")
    List<Incident> findByAssignedTeamMembersContaining(@Param("member") IncidentTeamMember member);

    // Metoda do wyszukiwania incydentów z danym zasobem
    @Query("SELECT i FROM Incident i JOIN i.affectedResources r WHERE r.id = :resourceId")
    List<Incident> findByAffectedResourceId(@Param("resourceId") Long resourceId);

    // Metoda do wyszukiwania incydentów z danego zakresu czasowego i priorytetu
    @Query("SELECT i FROM Incident i WHERE i.reportTime BETWEEN :start AND :end AND i.priority = :priority")
    List<Incident> findIncidentsByDateRangeAndPriority(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("priority") IncidentPriority priority
    );

    // Metoda do wyszukiwania incydentów po ID dispatchera
    @Query("SELECT i FROM Incident i WHERE i.registeredBy.id = :dispatcherId")
    List<Incident> findByDispatcherId(@Param("dispatcherId") Long dispatcherId);

    // Metoda do wyszukiwania incydentów po ID zespołu
    @Query("SELECT i FROM Incident i WHERE i.assignedTeam.id = :teamId")
    List<Incident> findByTeamId(@Param("teamId") Long teamId);

    // Metoda do wyszukiwania incydentów po lokalizacji
    @Query("SELECT i FROM Incident i WHERE i.location.id = :locationId")
    List<Incident> findByLocationId(@Param("locationId") Long locationId);

    // Metoda do liczenia incydentów według statusu
    @Query("SELECT i.status, COUNT(i) FROM Incident i GROUP BY i.status")
    List<Object[]> countIncidentsByStatus();

    // Metoda do liczenia incydentów według priorytetu
    @Query("SELECT i.priority, COUNT(i) FROM Incident i GROUP BY i.priority")
    List<Object[]> countIncidentsByPriority();

    // Metoda do wyszukiwania najnowszych incydentów
    @Query("SELECT i FROM Incident i ORDER BY i.reportTime DESC LIMIT :limit")
    List<Incident> findRecentIncidents(@Param("limit") int limit);

    // Metoda do wyszukiwania incydentów z danym tekstem w opisie
    @Query("SELECT i FROM Incident i WHERE LOWER(i.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Incident> searchByDescription(@Param("searchText") String searchText);

}