package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.entity.Dispatcher;
import org.example.securityservice.model.entity.SecurityManager;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeamMember;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.mapper.IncidentMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentPermissionService {

    private final IncidentMapper incidentMapper;

    public IncidentResponseDTO toResponseDtoWithPermissions(Incident incident) {
        IncidentResponseDTO dto = incidentMapper.toResponseDto(incident);


        return dto;
    }


    public List<Incident> filterIncidentsForTeamMember(List<Incident> incidents, IncidentTeamMember member) {
        return incidents.stream()
                .filter(incident -> incident.getAssignedTeam() != null &&
                        incident.getAssignedTeam().getMembers().contains(member))
                .collect(Collectors.toList());
    }


}