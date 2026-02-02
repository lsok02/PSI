package org.example.securityservice.repository;

import org.example.securityservice.model.entity.IncidentTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentTeamRepository extends JpaRepository<IncidentTeam, Long> {

    List<IncidentTeam> findBySpecialization(String specialization);

}