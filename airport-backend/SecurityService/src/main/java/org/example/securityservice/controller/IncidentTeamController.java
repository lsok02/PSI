package org.example.securityservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.service.IncidentTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class IncidentTeamController {

    private final IncidentTeamService incidentTeamService;

    @GetMapping
    public ResponseEntity<List<IncidentTeam>> getTeamsBySpecialization(
            @RequestParam String specialization) {

        List<IncidentTeam> incidentTeams = incidentTeamService.getIncidentTeamsBySpecialization(specialization);
        return ResponseEntity.ok(incidentTeams);

    }
}
