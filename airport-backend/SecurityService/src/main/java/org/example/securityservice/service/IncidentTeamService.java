package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.repository.IncidentTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentTeamService {

    private final IncidentTeamRepository incidentTeamRepository;

    public List<IncidentTeam> getIncidentTeamsBySpecialization(String specialization) {
        return incidentTeamRepository.findBySpecialization(specialization);
    }
}
