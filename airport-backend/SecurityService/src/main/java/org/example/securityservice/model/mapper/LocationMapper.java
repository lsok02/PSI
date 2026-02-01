package org.example.securityservice.model.mapper;

import org.example.securityservice.model.dto.LocationDTO;
import org.example.securityservice.model.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper implements BaseMapper<Location, LocationDTO> {

    @Override
    public LocationDTO toDto(Location entity) {
        if (entity == null) return null;

        LocationDTO dto = new LocationDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setCoordinates(entity.getCoordinates());
        return dto;
    }

    @Override
    public Location toEntity(LocationDTO dto) {
        if (dto == null) return null;

        return Location.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .coordinates(dto.getCoordinates())
                .build();
    }
}