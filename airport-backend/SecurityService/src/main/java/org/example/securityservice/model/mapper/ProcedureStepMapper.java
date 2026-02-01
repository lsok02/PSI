package org.example.securityservice.model.mapper;

import org.example.securityservice.model.dto.ProcedureStepDTO;
import org.example.securityservice.model.entity.ProcedureStep;
import org.springframework.stereotype.Component;

@Component
public class ProcedureStepMapper implements BaseMapper<ProcedureStep, ProcedureStepDTO> {

    @Override
    public ProcedureStepDTO toDto(ProcedureStep entity) {
        if (entity == null) return null;

        ProcedureStepDTO dto = new ProcedureStepDTO();
        dto.setId(entity.getId());
        dto.setStepNumber(entity.getStepOrder());
        dto.setDescription(entity.getStepDescription());

        return dto;
    }

    @Override
    public ProcedureStep toEntity(ProcedureStepDTO dto) {
        if (dto == null) return null;

        return ProcedureStep.builder()
                .id(dto.getId())
                .stepOrder(dto.getStepNumber())
                .stepDescription(dto.getDescription())
                .build();
    }
}