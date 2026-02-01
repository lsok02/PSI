package org.example.securityservice.model.mapper;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.IncidentTeamDTO;
import org.example.securityservice.model.entity.IncidentTeam;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IncidentTeamMapper implements BaseMapper<IncidentTeam, IncidentTeamDTO> {

    private final IncidentTeamMemberMapper teamMemberMapper;

    @Override
    public IncidentTeamDTO toDto(IncidentTeam entity) {
        if (entity == null) return null;

        IncidentTeamDTO dto = new IncidentTeamDTO();
        dto.setId(entity.getId());
        dto.setTeamName(entity.getTeamName());
        dto.setSpecialization(entity.getSpecialization());
        dto.setStatus(entity.getStatus());

//        if (entity.() != null) {
//            dto.setMembers(entity.getMembers().stream()
//                    .map(teamMemberMapper::toDto)
//                    .collect(Collectors.toList()));
//        }

        return dto;
    }

    @Override
    public IncidentTeam toEntity(IncidentTeamDTO dto) {
        if (dto == null) return null;

        IncidentTeam team = IncidentTeam.builder()
                .id(dto.getId())
                .teamName(dto.getTeamName())
                .specialization(dto.getSpecialization())
                .status(dto.getStatus())
                .build();

        // Uwaga: członkowie zespołu są zwykle zarządzani oddzielnie
        // poprzez dedykowane endpointy

        return team;
    }
}