package org.example.securityservice.repository;

import org.example.securityservice.model.entity.IncidentTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentTeamMemberRepository extends JpaRepository<IncidentTeamMember, Long> {

    // Znajdź wszystkich członków zespołu
    List<IncidentTeamMember> findByTeamId(Long teamId);

    // Znajdź członka po ID pracownika i ID zespołu
    Optional<IncidentTeamMember> findByEmployeeIdAndTeamId(Long employeeId, Long teamId);

    // Sprawdź czy pracownik jest członkiem jakiegoś zespołu
    boolean existsByEmployeeId(Long employeeId);

    // Pobierz wszystkich członków przypisanych do incydentu (poprzez zespół)
    @Query("SELECT itm FROM IncidentTeamMember itm " +
            "JOIN itm.team t " +
            "JOIN t.incidents i " +
            "WHERE i.id = :incidentId")
    List<IncidentTeamMember> findByIncidentId(@Param("incidentId") Long incidentId);

    // Pobierz członków po roli w zespole
    List<IncidentTeamMember> findByTeamRole(String teamRole);

    // Usuń wszystkich członków zespołu
    void deleteByTeamId(Long teamId);
}