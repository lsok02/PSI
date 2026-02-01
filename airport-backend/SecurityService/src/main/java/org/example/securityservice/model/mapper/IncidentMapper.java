package org.example.securityservice.model.mapper;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.IncidentDTO;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.entity.AirportResource;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.ReportingSource;
import org.example.securityservice.repository.AirportResourceRepository;
import org.example.securityservice.repository.DispatcherRepository;
import org.example.securityservice.repository.IncidentTeamRepository;
import org.example.securityservice.repository.LocationRepository;
import org.example.securityservice.repository.StandardOperatingProcedureRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IncidentMapper implements BaseMapper<Incident, IncidentDTO> {

    // Wstrzyknięcie wszystkich potrzebnych mapperów
    private final LocationMapper locationMapper;
    private final DispatcherMapper dispatcherMapper;
    private final IncidentTeamMapper incidentTeamMapper;
    private final AirportResourceMapper airportResourceMapper;
    private final LogEntryMapper logEntryMapper;
    private final StandardOperatingProcedureMapper sopMapper;

    // Repositories (tylko do mapowania z ID na encje)
    private final LocationRepository locationRepository;
    private final DispatcherRepository dispatcherRepository;
    private final IncidentTeamRepository incidentTeamRepository;
    private final AirportResourceRepository airportResourceRepository;
    private final StandardOperatingProcedureRepository sopRepository;

    @Override
    public IncidentDTO toDto(Incident entity) {
        if (entity == null) return null;

        IncidentDTO dto = new IncidentDTO();
        dto.setId(entity.getId());
        dto.setReportNumber(entity.getReportNumber());
        dto.setType(entity.getType());
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setReportSource(entity.getSource());
        dto.setDescription(entity.getDescription());
        dto.setCreationTime(entity.getReportTime());
        dto.setClosureTime(entity.getCloseTime());

        // Mapowanie ID zamiast pełnych obiektów
        if (entity.getLocation() != null) {
            dto.setLocationId(entity.getLocation().getId());
        }

        if (entity.getRegisteredBy() != null) {
            dto.setDispatcherId(entity.getRegisteredBy().getId());
        }

        if (entity.getAssignedTeam() != null) {
            dto.setAssignedTeamId(entity.getAssignedTeam().getId());
        }

        if (entity.getAffectedResources() != null) {
            dto.setAffectedResourceIds(entity.getAffectedResources().stream()
                    .map(AirportResource::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    public Incident toEntity(IncidentDTO dto) {
        if (dto == null) return null;

        Incident.IncidentBuilder builder = Incident.builder()
                .id(dto.getId())
                .reportNumber(dto.getReportNumber())
                .type(dto.getType())
                .priority(dto.getPriority())
                .status(dto.getStatus() != null ? dto.getStatus() : IncidentStatus.NEW)
                .source(dto.getReportSource() != null ? dto.getReportSource() : ReportingSource.MANUAL)
                .description(dto.getDescription())
                .reportTime(dto.getCreationTime() != null ? dto.getCreationTime() : LocalDateTime.now())
                .closeTime(dto.getClosureTime());

        // Mapowanie relacji z ID na encje
        if (dto.getLocationId() != null) {
            builder.location(locationRepository.findById(dto.getLocationId()).orElse(null));
        }

        if (dto.getDispatcherId() != null) {
            builder.registeredBy(dispatcherRepository.findById(dto.getDispatcherId()).orElse(null));
        }

        if (dto.getAssignedTeamId() != null) {
            builder.assignedTeam(incidentTeamRepository.findById(dto.getAssignedTeamId()).orElse(null));
        }

        if (dto.getAffectedResourceIds() != null) {
            List<AirportResource> resources = airportResourceRepository.findAllById(dto.getAffectedResourceIds());
            builder.affectedResources(resources);
        }

        // SOP może być ustawiony później na podstawie typu

        return builder.build();
    }

    // ========== MAPOWANIE DO RESPONSE DTO ==========

    public IncidentResponseDTO toResponseDto(Incident entity) {
        if (entity == null) return null;

        IncidentResponseDTO dto = new IncidentResponseDTO();
        dto.setId(entity.getId());
        dto.setReportNumber(entity.getReportNumber());
        dto.setType(entity.getType());
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setReportSource(entity.getSource());
        dto.setDescription(entity.getDescription());
        dto.setCreationTime(entity.getReportTime());
        dto.setClosureTime(entity.getCloseTime());

        // Mapowanie pełnych DTO dla relacji
        dto.setLocation(locationMapper.toDto(entity.getLocation()));
        dto.setRegisteredBy(dispatcherMapper.toDto(entity.getRegisteredBy()));
        dto.setAssignedTeam(incidentTeamMapper.toDto(entity.getAssignedTeam()));
        dto.setProcedure(sopMapper.toDto(entity.getSop()));

        // Mapowanie kolekcji
        if (entity.getAffectedResources() != null) {
            dto.setAffectedResources(entity.getAffectedResources().stream()
                    .map(airportResourceMapper::toDto)
                    .collect(Collectors.toList()));
        }

        if (entity.getHistory() != null) {
            dto.setJournalEntries(entity.getHistory().stream()
                    .map(logEntryMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}