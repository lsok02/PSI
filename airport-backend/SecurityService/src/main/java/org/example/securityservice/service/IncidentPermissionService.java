package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.mapper.IncidentMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncidentPermissionService {

    private final IncidentMapper incidentMapper;

    public IncidentResponseDTO toResponseDtoWithPermissions(Incident incident) {
        IncidentResponseDTO dto = incidentMapper.toResponseDto(incident);
        return dto;
    }

}