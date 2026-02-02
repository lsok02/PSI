package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.exception.BusinessRuleViolationException;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.TeamStatus;
import org.example.securityservice.repository.IncidentRepository;
import org.example.securityservice.repository.IncidentTeamRepository;
import org.example.securityservice.validator.TeamAssignmentValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamAssignmentService {

    private final IncidentTeamRepository teamRepository;
    private final IncidentRepository incidentRepository;
    private final TeamAssignmentValidator teamValidator;

    public IncidentTeam validateAndAssignTeam(Incident incident, Long teamId) {
        IncidentTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessRuleViolationException("Team not found with ID: " + teamId));

        teamValidator.validateTeamCanBeAssigned(team, incident);
        teamValidator.validateIncidentCanBeAssigned(incident);

        assignTeamToIncident(incident, team);
        return team;
    }

    public void assignTeamToIncident(Incident incident, IncidentTeam team) {
        incident.setAssignedTeam(team);
        incident.setStatus(IncidentStatus.ASSIGNED);
        team.setStatus(TeamStatus.BUSY);

        teamRepository.save(team);
        incidentRepository.save(incident);

        log.info("Team {} assigned to incident {}", team.getTeamName(), incident.getReportNumber());
    }
}