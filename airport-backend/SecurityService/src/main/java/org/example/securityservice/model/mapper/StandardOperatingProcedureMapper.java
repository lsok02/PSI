package org.example.securityservice.model.mapper;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.StandardOperatingProcedureDTO;
import org.example.securityservice.model.entity.ProcedureStep;
import org.example.securityservice.model.entity.StandardOperatingProcedure;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StandardOperatingProcedureMapper implements BaseMapper<StandardOperatingProcedure, StandardOperatingProcedureDTO> {

    private final ProcedureStepMapper procedureStepMapper;

    @Override
    public StandardOperatingProcedureDTO toDto(StandardOperatingProcedure entity) {
        if (entity == null) return null;

        StandardOperatingProcedureDTO dto = new StandardOperatingProcedureDTO();
        dto.setId(entity.getId());
        dto.setProcedureName(entity.getProcedureName());
        dto.setDescription(entity.getDescription());
        dto.setApplicableIncidentTypes(entity.getApplicableIncidentTypes());

        if (entity.getSteps() != null) {
            dto.setSteps(entity.getSteps().stream()
                    .map(procedureStepMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    public StandardOperatingProcedure toEntity(StandardOperatingProcedureDTO dto) {
        if (dto == null) return null;

        StandardOperatingProcedure sop = StandardOperatingProcedure.builder()
                .id(dto.getId())
                .procedureName(dto.getProcedureName())
                .description(dto.getDescription())
                .applicableIncidentTypes(dto.getApplicableIncidentTypes())
                .build();

        // Mapowanie kroków jeśli są dostarczone
        if (dto.getSteps() != null && !dto.getSteps().isEmpty()) {
            List<ProcedureStep> steps = dto.getSteps().stream()
                    .map(stepDto -> {
                        ProcedureStep step = procedureStepMapper.toEntity(stepDto);
                        step.setSop(sop); // Ustawienie relacji wstecznej
                        return step;
                    })
                    .collect(Collectors.toList());
            sop.setSteps(steps);
        }

        return sop;
    }
}