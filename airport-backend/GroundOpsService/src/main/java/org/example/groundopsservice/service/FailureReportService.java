package org.example.groundopsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.groundopsservice.client.AuthServiceClient;
import org.example.groundopsservice.client.SecurityIncidentClient;
import org.example.groundopsservice.client.dto.SecurityIncidentRequest;
import org.example.groundopsservice.client.dto.SecurityIncidentResponse;
import org.example.groundopsservice.model.dto.FailureReportRequest;
import org.example.groundopsservice.model.dto.FailureReportResponse;
import org.example.groundopsservice.model.entity.FailureReport;
import org.example.groundopsservice.model.entity.TechnicalResource;
import org.example.groundopsservice.model.enumeration.FailureStatus;
import org.example.groundopsservice.model.enumeration.FailureType;
import org.example.groundopsservice.model.enumeration.FailureUrgency;
import org.example.groundopsservice.model.enumeration.ResourceStatus;
import org.example.groundopsservice.repository.FailureReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FailureReportService {

    private final FailureReportRepository failureReportRepository;
    private final ResourceService resourceService;
    private final SecurityIncidentClient securityIncidentClient;
    private final AuthServiceClient authServiceClient;

    @Value("${integration.security.default-location-id:1}")
    private Long defaultSecurityLocationId;

    @Value("${integration.security.forward-token:false}")
    private boolean forwardToken;

    public FailureReportResponse reportFailure(FailureReportRequest request, String token) {
        if (request.getResourceId() == null) {
            throw new IllegalArgumentException("Resource ID is required");
        }
        if (request.getFailureType() == null) {
            throw new IllegalArgumentException("Failure type is required");
        }
        if (request.getUrgency() == null) {
            throw new IllegalArgumentException("Urgency is required");
        }
        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        TechnicalResource resource = resourceService.getResource(request.getResourceId());

        String reporter = authServiceClient.getUsernameFromToken(token);

        FailureReport report = new FailureReport();
        report.setResource(resource);
        report.setFailureType(request.getFailureType());
        report.setDescription(request.getDescription());
        report.setUrgency(request.getUrgency());
        report.setLocation(request.getLocation());
        report.setReportedAt(LocalDateTime.now());
        report.setStatus(FailureStatus.REPORTED);
        report.setReportedBy(reporter != null ? reporter : "system");

        FailureReport saved = failureReportRepository.save(report);

        if (resource.getStatus() != ResourceStatus.OUT_OF_ORDER) {
            resource.setStatus(ResourceStatus.OUT_OF_ORDER);
            resourceService.saveResource(resource);
        }

        try {
            Long locationId = request.getSecurityLocationId() != null
                    ? request.getSecurityLocationId()
                    : defaultSecurityLocationId;

            SecurityIncidentRequest incidentRequest = new SecurityIncidentRequest(
                    mapIncidentType(saved.getFailureType()),
                    mapIncidentPriority(saved.getUrgency()),
                    locationId,
                    buildIncidentDescription(saved, resource),
                    "SYSTEM"
            );

            String tokenToForward = forwardToken ? token : null;
            SecurityIncidentResponse response = securityIncidentClient.createIncident(incidentRequest, tokenToForward);
            if (response != null && response.getId() != null) {
                saved.setSecurityIncidentId(response.getId());
                failureReportRepository.save(saved);
            }
        } catch (Exception ex) {
            log.error("Failed to create security incident for failure {}", saved.getId(), ex);
            throw ex;
        }

        return toResponse(saved);
    }

    public List<FailureReportResponse> getFailures() {
        return failureReportRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private FailureReportResponse toResponse(FailureReport report) {
        return FailureReportResponse.builder()
                .id(report.getId())
                .resourceId(report.getResource() != null ? report.getResource().getId() : null)
                .resourceName(report.getResource() != null ? report.getResource().getName() : null)
                .failureType(report.getFailureType() != null ? report.getFailureType().name() : null)
                .description(report.getDescription())
                .urgency(report.getUrgency() != null ? report.getUrgency().name() : null)
                .location(report.getLocation())
                .status(report.getStatus() != null ? report.getStatus().name() : null)
                .reportedAt(report.getReportedAt())
                .reportedBy(report.getReportedBy())
                .securityIncidentId(report.getSecurityIncidentId())
                .build();
    }

    private String mapIncidentType(FailureType type) {
        if (type == null) {
            return "TECHNICAL_EQUIPMENT_FAILURE";
        }
        return switch (type) {
            case SOFTWARE -> "TECHNICAL_IT_SYSTEM_FAILURE";
            case COMMUNICATION -> "TECHNICAL_COMMUNICATION_FAILURE";
            case POWER_OUTAGE -> "TECHNICAL_POWER_OUTAGE";
            case WATER_LEAKAGE -> "TECHNICAL_WATER_LEAKAGE";
            default -> "TECHNICAL_EQUIPMENT_FAILURE";
        };
    }

    private String mapIncidentPriority(FailureUrgency urgency) {
        if (urgency == null) {
            return "NORMAL";
        }
        return urgency.name().toUpperCase(Locale.ROOT);
    }

    private String buildIncidentDescription(FailureReport report, TechnicalResource resource) {
        String resourceLabel = resource.getName() != null ? resource.getName() : "Resource";
        String resourceType = resource.getResourceType() != null ? resource.getResourceType().name() : "UNKNOWN";
        String location = report.getLocation() != null ? report.getLocation() : "Unknown location";
        String description = report.getDescription() != null ? report.getDescription() : "No description provided";

        return "Equipment failure reported (failureId=" + report.getId() + "). " +
                "Resource: " + resourceLabel + " [id=" + resource.getId() + ", type=" + resourceType + "]. " +
                "Location: " + location + ". " +
                "Details: " + description;
    }
}
