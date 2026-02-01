package org.example.securityservice.repository;

import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.TeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentTeamRepository extends JpaRepository<IncidentTeam, Long> {

    List<IncidentTeam> findByStatus(TeamStatus status);

    List<IncidentTeam> findBySpecialization(String specialization);

    List<IncidentTeam> findByStatusAndSpecialization(TeamStatus status, String specialization);

}