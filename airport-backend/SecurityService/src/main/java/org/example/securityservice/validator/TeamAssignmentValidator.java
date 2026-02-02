package org.example.securityservice.validator;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.exception.BusinessRuleViolationException;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.TeamStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamAssignmentValidator {

    public void validateTeamQualification(IncidentTeam team, Incident incident) {
        if (!team.getSpecialization().equals(incident.getType())) {
            throw new BusinessRuleViolationException(
                    "Team specialization (" + team.getSpecialization() + ") " +
                            "does not match incident type (" + incident.getType() + ")");
        }
    }

    public void validateTeamAvailability(IncidentTeam team) {
        if (team.getStatus() != TeamStatus.AVAILABLE) {
            throw new BusinessRuleViolationException("Team is not available. Current status: " + team.getStatus());
        }
    }

    public void validateTeamCanBeAssigned(IncidentTeam team, Incident incident) {
        validateTeamExists(team);
        validateTeamQualification(team, incident);
        validateTeamAvailability(team);
    }

    public void validateIncidentCanBeAssigned(Incident incident) {
        if (incident.getStatus() != IncidentStatus.NEW) {
            throw new BusinessRuleViolationException(
                    "Only NEW incidents can be assigned to a team. Current status: " + incident.getStatus());
        }

        if (incident.getAssignedTeam() != null) {
            throw new BusinessRuleViolationException(
                    "Incident is already assigned to team: " + incident.getAssignedTeam().getTeamName());
        }
    }

    private void validateTeamExists(IncidentTeam team) {
        if (team == null) {
            throw new BusinessRuleViolationException("Team cannot be null");
        }
    }
}