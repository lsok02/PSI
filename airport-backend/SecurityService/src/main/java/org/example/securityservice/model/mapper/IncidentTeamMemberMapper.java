package org.example.securityservice.model.mapper;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.IncidentTeamMemberDTO;
import org.example.securityservice.model.entity.IncidentTeamMember;
import org.springframework.stereotype.Component;

@Component
public class IncidentTeamMemberMapper implements BaseMapper<IncidentTeamMember, IncidentTeamMemberDTO> {

    @Override
    public IncidentTeamMemberDTO toDto(IncidentTeamMember entity) {
        if (entity == null) return null;

        IncidentTeamMemberDTO dto = new IncidentTeamMemberDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setServiceNumber(entity.getServiceNumber());
        dto.setRadioCallSign(entity.getRadioId());

        if (entity.getTeam() != null) {
            dto.setCurrentTeam(entity.getTeam());
        }

        return dto;
    }

    @Override
    public IncidentTeamMember toEntity(IncidentTeamMemberDTO dto) {
        if (dto == null) return null;

        return (IncidentTeamMember) IncidentTeamMember.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .serviceNumber(dto.getServiceNumber())
                .build();
        // Uwaga: team jest ustawiany oddzielnie
    }
}