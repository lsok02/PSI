package org.example.securityservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.IncidentTeamDTO;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.mapper.IncidentTeamMapper;
import org.example.securityservice.service.IncidentTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/security/teams")
@RequiredArgsConstructor
public class IncidentTeamController {

    private final IncidentTeamService incidentTeamService;
    private final IncidentTeamMapper incidentTeamMapper;

    @GetMapping
    public ResponseEntity<List<IncidentTeamDTO>> getTeamsBySpecialization(
            @RequestParam IncidentType specialization) {

        List<IncidentTeam> incidentTeams = incidentTeamService
                .getIncidentTeamsBySpecialization(specialization);

        List<IncidentTeamDTO> result = incidentTeams.stream()
                .map(incidentTeamMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
