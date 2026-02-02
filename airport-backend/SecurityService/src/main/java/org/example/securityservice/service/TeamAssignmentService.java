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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamAssignmentService {

    private final IncidentTeamRepository teamRepository;
    private final IncidentRepository incidentRepository;

    public IncidentTeam validateAndAssignTeam(Incident incident, Long teamId) {
        IncidentTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessRuleViolationException("Team not found"));

        validateTeamQualification(team, incident);
        validateTeamAvailability(team);

        assignTeamToIncident(incident, team);
        return team;
    }

    private void validateTeamQualification(IncidentTeam team, Incident incident) {
        if (!team.getSpecialization().equals(incident.getType())) {
            throw new BusinessRuleViolationException(
                    "Team specialization (" + team.getSpecialization() + ") " +
                            "does not match incident type (" + incident.getType() + ")");
        }
    }

    private void validateTeamAvailability(IncidentTeam team) {
        if (team.getStatus() != TeamStatus.AVAILABLE) {
            throw new BusinessRuleViolationException("Team is not available. Current status: " + team.getStatus());
        }
    }

    private void assignTeamToIncident(Incident incident, IncidentTeam team) {
        incident.setAssignedTeam(team);
        incident.setStatus(IncidentStatus.ASSIGNED);
        team.setStatus(TeamStatus.BUSY);
        teamRepository.save(team);
    }

    public void autoAssignCriticalIncident(Incident incident) {
        try {
            List<IncidentTeam> availableTeams = teamRepository.findByStatusAndSpecialization(
                    TeamStatus.AVAILABLE, incident.getType());

            if (!availableTeams.isEmpty()) {
                IncidentTeam team = availableTeams.get(0);
                assignTeamToIncident(incident, team);
                log.info("Auto-assigned critical incident {} to team {}",
                        incident.getReportNumber(), team.getTeamName());
            }
        } catch (Exception e) {
            log.warn("Failed to auto-assign critical incident: {}", e.getMessage());
        }
    }

    /**
     * Releases a team back to AVAILABLE status when an incident is closed or
     * resolved.
     */
    public void releaseTeam(IncidentTeam team) {
        if (team != null && team.getStatus() == TeamStatus.BUSY) {
            team.setStatus(TeamStatus.AVAILABLE);
            teamRepository.save(team);
            log.info("Team {} released and now AVAILABLE", team.getTeamName());
        }
    }
}