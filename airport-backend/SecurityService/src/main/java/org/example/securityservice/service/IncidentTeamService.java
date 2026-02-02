package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.repository.IncidentTeamRepository;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.TeamStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentTeamService {

    private final IncidentTeamRepository incidentTeamRepository;

    /**
     * Returns only AVAILABLE teams with the given specialization.
     * Teams that are BUSY or UNAVAILABLE are not included.
     */
    public List<IncidentTeam> getIncidentTeamsBySpecialization(IncidentType specialization) {
        return incidentTeamRepository.findByStatusAndSpecialization(TeamStatus.AVAILABLE, specialization);
    }
}
