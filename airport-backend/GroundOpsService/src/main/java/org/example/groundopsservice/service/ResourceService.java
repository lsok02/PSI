package org.example.groundopsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.groundopsservice.model.dto.ResourceSummaryDTO;
import org.example.groundopsservice.model.entity.SpecializedEquipment;
import org.example.groundopsservice.model.entity.TechnicalResource;
import org.example.groundopsservice.model.entity.Vehicle;
import org.example.groundopsservice.repository.TechnicalResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final TechnicalResourceRepository technicalResourceRepository;

    public List<ResourceSummaryDTO> getResources() {
        return technicalResourceRepository.findAll()
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    public TechnicalResource getResource(Long id) {
        return technicalResourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));
    }

    public TechnicalResource saveResource(TechnicalResource resource) {
        return technicalResourceRepository.save(resource);
    }

    private ResourceSummaryDTO toSummary(TechnicalResource resource) {
        String details = null;
        if (resource instanceof Vehicle vehicle) {
            details = "Reg: " + nullSafe(vehicle.getRegistrationNumber()) +
                    " • Type: " + nullSafe(vehicle.getVehicleType());
        } else if (resource instanceof SpecializedEquipment equipment) {
            details = "Category: " + nullSafe(equipment.getCategory());
        }

        return ResourceSummaryDTO.builder()
                .id(resource.getId())
                .name(resource.getName())
                .status(resource.getStatus() != null ? resource.getStatus().name() : null)
                .resourceType(resource.getResourceType() != null ? resource.getResourceType().name() : null)
                .details(details)
                .nextMaintenanceDate(resource.getNextMaintenanceDate())
                .build();
    }

    private String nullSafe(String value) {
        return value == null ? "n/a" : value;
    }
}
