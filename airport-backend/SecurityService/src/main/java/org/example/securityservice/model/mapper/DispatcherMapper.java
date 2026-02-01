package org.example.securityservice.model.mapper;

import org.example.securityservice.model.dto.DispatcherDTO;
import org.example.securityservice.model.entity.Dispatcher;
import org.springframework.stereotype.Component;

@Component
public class DispatcherMapper implements BaseMapper<Dispatcher, DispatcherDTO> {

    @Override
    public DispatcherDTO toDto(Dispatcher entity) {
        if (entity == null) return null;

        DispatcherDTO dto = new DispatcherDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setServiceNumber(entity.getServiceNumber());

        if (entity.getRegisteredIncidents() != null) {
            dto.setIncidentsRegisteredCount(entity.getRegisteredIncidents().size());
        }

        return dto;
    }

    @Override
    public Dispatcher toEntity(DispatcherDTO dto) {
        if (dto == null) return null;
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setId(dto.getId());
        dispatcher.setFirstName(dto.getFirstName());
        dispatcher.setLastName(dto.getLastName());
        dispatcher.setServiceNumber(dto.getServiceNumber());

        return dispatcher;
    }
}