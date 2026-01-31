package org.example.securityservice.model.mapper;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.LogEntryDTO;
import org.example.securityservice.model.entity.LogEntry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogEntryMapper implements BaseMapper<LogEntry, LogEntryDTO> {

    private final EmployeeMapper employeeMapper;

    @Override
    public LogEntryDTO toDto(LogEntry entity) {
        if (entity == null) return null;

        LogEntryDTO dto = new LogEntryDTO();
        dto.setId(entity.getId());
        dto.setActionTime(entity.getActionTime());
        dto.setActionDescription(entity.getActionDescription());

        if (entity.getPerformedBy() != null) {
            dto.setPerformedBy(employeeMapper.toDto(entity.getPerformedBy()));
        }

        if (entity.getIncident() != null) {
            dto.setIncidentId(entity.getIncident().getId());
        }

        return dto;
    }

    @Override
    public LogEntry toEntity(LogEntryDTO dto) {
        if (dto == null) return null;

        return LogEntry.builder()
                .id(dto.getId())
                .actionTime(dto.getActionTime())
                .actionDescription(dto.getActionDescription())
                .build();
    }
}