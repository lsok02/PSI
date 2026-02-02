package org.example.securityservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.exception.BusinessRuleViolationException;
import org.example.securityservice.model.dto.IncidentDTO;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.dto.StatusChangeDTO;
import org.example.securityservice.model.entity.Dispatcher;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.SecurityManager;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.ReportingSource;
import org.example.securityservice.repository.DispatcherRepository;
import org.example.securityservice.repository.IncidentRepository;
import org.example.securityservice.validator.IncidentValidator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final DispatcherRepository dispatcherRepository;
    private final IncidentValidator validator;
    private final IncidentPermissionService permissionService;
    private final IncidentNotificationService notificationService;
    private final TeamAssignmentService teamAssignmentService;
    private final SensorEventService sensorEventService;

    @Transactional
    public IncidentResponseDTO createIncident(IncidentDTO incidentDTO, Long currentUserId) {
        log.info("Creating incident by user ID: {}", currentUserId);

        Employee currentUser = null;
        Dispatcher dispatcher;

        if (currentUserId != null) {
            currentUser = validator.validateAndGetUser(currentUserId);
            validator.validateUserCanCreateIncident(currentUser);
            dispatcher = getDispatcherForIncident(currentUser);
        } else {
            // For API calls without user context, use first available dispatcher
            dispatcher = dispatcherRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new BusinessRuleViolationException("No dispatcher available"));
            currentUser = dispatcher;
        }

        validator.validateIncidentCreation(incidentDTO);

        String reportNumber = generateReportNumber();

        Incident incident = Incident.builder()
                .reportNumber(reportNumber)
                .type(incidentDTO.getType())
                .priority(incidentDTO.getPriority())
                .status(IncidentStatus.NEW)
                .source(incidentDTO.getReportSource() != null ? incidentDTO.getReportSource() : ReportingSource.MANUAL)
                .description(incidentDTO.getDescription())
                .reportTime(LocalDateTime.now())
                .registeredBy(dispatcher)
                .build();

        if (incidentDTO.getLocationId() != null) {
            incident.setLocation(validator.validateLocationExists(incidentDTO.getLocationId()));
        }

        Incident savedIncident = incidentRepository.save(incident);
        updateDispatcherIncidents(dispatcher, savedIncident);

        notificationService.createInitialLog(savedIncident, currentUser, incidentDTO.getDescription());

        log.info("SensorEventId from DTO: {}", incidentDTO.getSensorEventId());
        if (incidentDTO.getSensorEventId() != null) {
            sensorEventService.addIncidentForAlarm(incidentDTO.getSensorEventId(),
                    permissionService.toResponseDtoWithPermissions(savedIncident));
        }

        log.info("Incident created successfully: {}", savedIncident.getReportNumber());
        return permissionService.toResponseDtoWithPermissions(savedIncident);
    }

    @Transactional
    public IncidentResponseDTO assignTeam(Long incidentId, Long teamId, Long currentUserId) {
        log.info("Assigning team to incident {} by user {}", incidentId, currentUserId);

        Incident incident = validator.validateAndGetIncident(incidentId);
        Employee currentUser = validator.validateAndGetUser(currentUserId);

        validator.validateUserCanAssignTeam(currentUser);
        validator.validateIncidentCanBeAssigned(incident);

        IncidentTeam team = teamAssignmentService.validateAndAssignTeam(incident, teamId);
        incident.setAssignedTeam(team);
        incident.setStatus(IncidentStatus.ASSIGNED);

        Incident updatedIncident = incidentRepository.save(incident);

        notificationService.createTeamAssignmentLog(updatedIncident, currentUser, team);

        log.info("Team {} assigned to incident {}", team.getTeamName(), updatedIncident.getReportNumber());
        return permissionService.toResponseDtoWithPermissions(updatedIncident);
    }

    @Transactional
    public IncidentResponseDTO updateStatus(Long incidentId, StatusChangeDTO statusChangeDTO, Long currentUserId) {
        log.info("Updating status of incident {} to {} by user {}",
                incidentId, statusChangeDTO.getNewStatus(), currentUserId);

        Incident incident = validator.validateAndGetIncident(incidentId);
        Employee currentUser = validator.validateAndGetUser(currentUserId);

        validator.validateUserCanChangeStatus(currentUser, incident);

        IncidentStatus oldStatus = incident.getStatus();
        IncidentStatus newStatus = statusChangeDTO.getNewStatus();
        incident.setStatus(newStatus);

        // Release team when incident is closed or resolved
        if ((newStatus == IncidentStatus.CLOSED || newStatus == IncidentStatus.RESOLVED)
                && incident.getAssignedTeam() != null) {
            teamAssignmentService.releaseTeam(incident.getAssignedTeam());
        }

        Incident updatedIncident = incidentRepository.save(incident);

        notificationService.createStatusChangeLog(updatedIncident, currentUser, oldStatus, statusChangeDTO);

        log.info("Incident {} status updated from {} to {}",
                updatedIncident.getReportNumber(), oldStatus, newStatus);

        return permissionService.toResponseDtoWithPermissions(updatedIncident);
    }

    @Transactional
    public List<IncidentResponseDTO> getIncidents(IncidentStatus status, IncidentPriority priority,
                                                  IncidentType type, LocalDateTime from, LocalDateTime to,
                                                  Long userId) {
        log.debug("Getting incidents with filters: status={}, priority={}, type={}", status, priority, type);

        Specification<Incident> spec = IncidentSpecifications.hasStatus(status)
                .and(IncidentSpecifications.hasPriority(priority))
                .and(IncidentSpecifications.hasType(type))
                .and(IncidentSpecifications.reportedAfter(from))
                .and(IncidentSpecifications.reportedBefore(to));

        List<Incident> incidents = incidentRepository.findAll(spec);

        return incidents.stream()
                .map(incident -> permissionService.toResponseDtoWithPermissions(incident))
                .collect(Collectors.toList());
    }

    @Transactional
    public IncidentResponseDTO getIncidentById(Long id, Long userId) {
        Incident incident = validator.validateAndGetIncident(id);
        return permissionService.toResponseDtoWithPermissions(incident);
    }

    @Transactional
    public IncidentResponseDTO updateIncident(Long incidentId, IncidentDTO incidentDTO, Long currentUserId) {
        log.info("Updating incident {} by user {}", incidentId, currentUserId);

        Incident existingIncident = validator.validateAndGetIncident(incidentId);
        Employee currentUser = validator.validateAndGetUser(currentUserId);

        if (incidentDTO.getDescription() != null) {
            existingIncident.setDescription(incidentDTO.getDescription());
        }

        if (incidentDTO.getPriority() != null &&
                (currentUser instanceof Dispatcher || currentUser instanceof SecurityManager)) {
            existingIncident.setPriority(incidentDTO.getPriority());
        }

        if (incidentDTO.getLocationId() != null) {
            existingIncident.setLocation(validator.validateLocationExists(incidentDTO.getLocationId()));
        }

        Incident updatedIncident = incidentRepository.save(existingIncident);
        notificationService.createUpdateLog(updatedIncident, currentUser);

        log.info("Incident {} updated by user {}", incidentId, currentUserId);
        return permissionService.toResponseDtoWithPermissions(updatedIncident);
    }

    @Transactional
    public IncidentResponseDTO escalateIncident(Long incidentId, Long currentUserId) {
        log.info("Escalating incident {} by user {}", incidentId, currentUserId);

        Incident incident = validator.validateAndGetIncident(incidentId);
        Employee currentUser = validator.validateAndGetUser(currentUserId);

        IncidentPriority oldPriority = incident.getPriority();
        IncidentPriority newPriority = bumpPriority(oldPriority);

        if (oldPriority != newPriority) {
            incident.setPriority(newPriority);
            Incident updatedIncident = incidentRepository.save(incident);

            notificationService.createEscalationLog(updatedIncident, currentUser, oldPriority, newPriority);

            log.info("Incident {} escalated from {} to {} by user {}",
                    incidentId, oldPriority, newPriority, currentUserId);
            return permissionService.toResponseDtoWithPermissions(updatedIncident);
        }

        return permissionService.toResponseDtoWithPermissions(incident);
    }

    private IncidentPriority bumpPriority(IncidentPriority priority) {
        if (priority == null)
            return IncidentPriority.NORMAL;
        switch (priority) {
            case LOW:
                return IncidentPriority.NORMAL;
            case NORMAL:
                return IncidentPriority.HIGH;
            case HIGH:
            case CRITICAL:
                return IncidentPriority.CRITICAL;
            default:
                return priority;
        }
    }

    private Dispatcher getDispatcherForIncident(Employee user) {
        if (user instanceof Dispatcher) {
            return (Dispatcher) user;
        } else if (user instanceof SecurityManager) {
            return dispatcherRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new BusinessRuleViolationException("No dispatcher available"));
        }
        throw new BusinessRuleViolationException("Cannot determine dispatcher for user");
    }

    private void updateDispatcherIncidents(Dispatcher dispatcher, Incident incident) {
        if (dispatcher != null) {
            if (dispatcher.getRegisteredIncidents() == null) {
                dispatcher.setRegisteredIncidents(new ArrayList<>());
            }
            dispatcher.getRegisteredIncidents().add(incident);
        }
    }

    private String generateReportNumber() {
        return "INC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() +
                "-" + LocalDateTime.now().getYear();
    }
}