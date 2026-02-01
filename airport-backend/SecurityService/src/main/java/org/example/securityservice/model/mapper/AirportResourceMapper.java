package org.example.securityservice.model.mapper;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.dto.AirportResourceDTO;
import org.example.securityservice.model.entity.AirportResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirportResourceMapper implements BaseMapper<AirportResource, AirportResourceDTO> {

    private final LocationMapper locationMapper;

    @Override
    public AirportResourceDTO toDto(AirportResource entity) {
        if (entity == null) return null;

        AirportResourceDTO dto = new AirportResourceDTO();
        dto.setId(entity.getId());
        dto.setResourceName(entity.getResourceName());
        dto.setResourceType(entity.getResourceType());

        return dto;
    }

    @Override
    public AirportResource toEntity(AirportResourceDTO dto) {
        if (dto == null) return null;

        return AirportResource.builder()
                .id(dto.getId())
                .resourceName(dto.getResourceName())
                .resourceType(dto.getResourceType())
                .build();
    }
}