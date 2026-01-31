package org.example.securityservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.exception.BusinessRuleViolationException;
import org.example.securityservice.exception.IncidentNotFoundException;
import org.example.securityservice.model.dto.AirportResourceDTO;
import org.example.securityservice.model.dto.ClosureReportDTO;
import org.example.securityservice.model.dto.DispatcherDTO;
import org.example.securityservice.model.dto.FlightDTO;
import org.example.securityservice.model.dto.IncidentDTO;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.dto.IncidentTeamDTO;
import org.example.securityservice.model.dto.IncidentTeamMemberDTO;
import org.example.securityservice.model.dto.LocationDTO;
import org.example.securityservice.model.dto.LogEntryDTO;
import org.example.securityservice.model.dto.SecurityManagerDTO;
import org.example.securityservice.model.dto.SensorEventDTO;
import org.example.securityservice.model.dto.StandardOperatingProcedureDTO;
import org.example.securityservice.model.entity.AirportResource;
import org.example.securityservice.model.entity.AuditLog;
import org.example.securityservice.model.entity.ClosureReport;
import org.example.securityservice.model.entity.Dispatcher;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeam;
import org.example.securityservice.model.entity.IncidentTeamMember;
import org.example.securityservice.model.entity.Location;
import org.example.securityservice.model.entity.LogEntry;
import org.example.securityservice.model.entity.SensorEvent;
import org.example.securityservice.model.entity.StandardOperatingProcedure;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.ReportingSource;
import org.example.securityservice.model.enumeration.TeamStatus;
import org.example.securityservice.repository.AirportResourceRepository;
import org.example.securityservice.repository.AuditLogRepository;
import org.example.securityservice.repository.ClosureReportRepository;
import org.example.securityservice.repository.DispatcherRepository;
import org.example.securityservice.repository.EmployeeRepository;
import org.example.securityservice.repository.IncidentRepository;
import org.example.securityservice.repository.IncidentTeamMemberRepository;
import org.example.securityservice.repository.IncidentTeamRepository;
import org.example.securityservice.repository.LocationRepository;
import org.example.securityservice.repository.LogEntryRepository;
import org.example.securityservice.repository.SensorEventRepository;
import org.example.securityservice.repository.StandardOperatingProcedureRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {

    // Repositories
    private final IncidentRepository incidentRepository;
    private final IncidentTeamRepository teamRepository;
    private final LocationRepository locationRepository;
    private final EmployeeRepository employeeRepository;
    private final DispatcherRepository dispatcherRepository;
    private final IncidentTeamMemberRepository teamMemberRepository;
    private final AirportResourceRepository resourceRepository;
    private final StandardOperatingProcedureRepository sopRepository;
    private final LogEntryRepository logEntryRepository;
    private final AuditLogRepository auditLogRepository;
    private final ClosureReportRepository closureReportRepository;
    private final SensorEventRepository sensorEventRepository;

    // Services
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    // External clients
    private final FlightServiceClient flightServiceClient;


    @Transactional
    public IncidentResponseDTO createIncident(IncidentDTO incidentDTO, Long currentUserId) {
        log.info("Creating incident by user ID: {}", currentUserId);

        // Get current user with proper type checking
        Employee currentUser = employeeRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessRuleViolationException("User not found"));

        // Validate user can create incidents
        if (!canCreateIncident(currentUser)) {
            throw new BusinessRuleViolationException(
                    "User does not have permission to create incidents. " +
                            "Only Dispatchers and Security Managers can create incidents.");
        }

        // Validate incident data
        validateIncidentCreation(incidentDTO);

        // Generate unique report number
        String reportNumber = generateReportNumber();

        // Get dispatcher for registration
        Dispatcher dispatcher = getDispatcherForIncident(currentUser);

        // Create incident
        Incident incident = Incident.builder()
                .reportNumber(reportNumber)
                .type(incidentDTO.getType())
                .priority(incidentDTO.getPriority())
                .status(IncidentStatus.NEW)
                .source(incidentDTO.getReportSource() != null ?
                        incidentDTO.getReportSource() : ReportingSource.MANUAL)
                .description(incidentDTO.getDescription())
                .reportTime(LocalDateTime.now())
                .registeredBy(dispatcher)
                .build();

        // Set location
        if (incidentDTO.getLocationId() != null) {
            Location location = locationRepository.findById(incidentDTO.getLocationId())
                    .orElseThrow(() -> new BusinessRuleViolationException("Location not found"));
            incident.setLocation(location);
        }

        // Set affected resources
        if (incidentDTO.getAffectedResourceIds() != null && !incidentDTO.getAffectedResourceIds().isEmpty()) {
            List<AirportResource> resources = resourceRepository.findAllById(incidentDTO.getAffectedResourceIds());
            incident.setAffectedResources(resources);
        }

        // Set SOP based on incident type
        StandardOperatingProcedure sop = sopRepository.findByIncidentType(incidentDTO.getType())
                .orElse(null);
        incident.setProcedure(sop);

        // Save incident
        Incident savedIncident = incidentRepository.save(incident);

        // Update dispatcher's incident list
        if (dispatcher != null) {
            if (dispatcher.getRegisteredIncidents() == null) {
                dispatcher.setRegisteredIncidents(new ArrayList<>());
            }
            dispatcher.getRegisteredIncidents().add(savedIncident);
            dispatcherRepository.save(dispatcher);
        }

        // Add initial log entry
        LogEntry initialLog = LogEntry.builder()
                .incident(savedIncident)
                .performedBy(currentUser)
                .actionTime(LocalDateTime.now())
                .actionDescription("Incident created")
                .actionDescription(incidentDTO.getDescription())
                .build();
        logEntryRepository.save(initialLog);

        // Log audit
        auditLogService.logIncidentCreation(savedIncident, currentUser);

        // Check for critical incidents affecting flights
        if (savedIncident.getPriority() == IncidentPriority.CRITICAL) {
            checkAffectedFlights(savedIncident);
        }

        // Send notifications for high/critical priority
//        if (savedIncident.getPriority() == IncidentPriority.CRITICAL ||
//                savedIncident.getPriority() == IncidentPriority.HIGH) {
//            notificationService.sendNewIncidentNotification(savedIncident);
//        }

        log.info("Incident created successfully: {}", savedIncident.getReportNumber());
        return convertToResponseDTO(savedIncident, currentUser);
    }

    @Transactional
    public IncidentResponseDTO createIncidentFromSensor(SensorEventDTO sensorEvent) {
        log.info("Creating incident from sensor event: {}", sensorEvent.getAlarmType());

        IncidentPriority priority = determinePriorityFromSensor(sensorEvent.getAlarmType());
        IncidentType type = determineTypeFromSensor(sensorEvent.getAlarmType());

        // Create sensor event entity
        SensorEvent event = SensorEvent.builder()
                .sensorId(sensorEvent.getSensorId())
                .zoneCode(sensorEvent.getZoneCode())
                .alarmType(sensorEvent.getAlarmType())
                .additionalData(sensorEvent.getAdditionalData())
                .detectionTime(LocalDateTime.now().minusSeconds(30)) // Simulate detection time
                .receivedTime(LocalDateTime.now())
                .processed(true)
                .build();

        // Find location by zone code
        Location location = locationRepository.findByCode(sensorEvent.getZoneCode())
                .orElseThrow(() -> new BusinessRuleViolationException("Zone not found: " + sensorEvent.getZoneCode()));

        // Get default dispatcher
        Dispatcher defaultDispatcher = dispatcherRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new BusinessRuleViolationException("No dispatcher available"));

        // Create incident
        Incident incident = Incident.builder()
                .reportNumber(generateReportNumber())
                .type(type)
                .priority(priority)
                .status(IncidentStatus.NEW)
                .source(ReportingSource.SYSTEM)
                .creationTime(LocalDateTime.now())
                .description(String.format("Sensor alert: %s in zone %s (%s)",
                        sensorEvent.getAlarmType(), sensorEvent.getZoneCode(), location.getName()))
                .registeredBy(defaultDispatcher)
                .location(location)
                .build();

        // Set SOP
        StandardOperatingProcedure sop = sopRepository.findByIncidentType(type)
                .orElse(null);
        incident.setProcedure(sop);

        // Save incident
        Incident savedIncident = incidentRepository.save(incident);

        // Link sensor event to incident
        event.setIncident(savedIncident);
        sensorEventRepository.save(event);

        // Add log entry
        LogEntry logEntry = LogEntry.builder()
                .incident(savedIncident)
                .actionTime(LocalDateTime.now())
                .actionDescription("Incident created from sensor event")
                .details(String.format("Sensor: %s, Alarm: %s",
                        sensorEvent.getSensorId(), sensorEvent.getAlarmType()))
                .build();
        logEntryRepository.save(logEntry);

        // Update dispatcher
        if (defaultDispatcher.getRegisteredIncidents() == null) {
            defaultDispatcher.setRegisteredIncidents(new ArrayList<>());
        }
        defaultDispatcher.getRegisteredIncidents().add(savedIncident);
        dispatcherRepository.save(defaultDispatcher);

        // Critical sensor events require immediate notification
        if (priority == IncidentPriority.CRITICAL) {
            notificationService.sendCriticalAlert(savedIncident);

            // For fire alerts, automatically trigger flight operations block
            if (sensorEvent.getAlarmType().contains("FIRE") ||
                    sensorEvent.getAlarmType().contains("SMOKE")) {
                blockFlightOperationsIfNeeded(savedIncident);
            }

            // Auto-assign to appropriate team if available
            autoAssignCriticalIncident(savedIncident);
        }

        auditLogService.logSensorIncidentCreation(savedIncident);
        log.info("Sensor incident created: {}", savedIncident.getReportNumber());

        // Return response with null user (system action)
        return convertToResponseDTO(savedIncident, null);
    }

    @Transactional
    public IncidentResponseDTO assignTeam(Long incidentId, TeamAssignmentDTO assignmentDTO, Long currentUserId) {
        log.info("Assigning team to incident {} by user {}", incidentId, currentUserId);

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentNotFoundException(incidentId));

        // Get current user
        Employee currentUser = employeeRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessRuleViolationException("User not found"));

        // Validate user can assign teams
        if (!canAssignTeam(currentUser)) {
            throw new BusinessRuleViolationException(
                    "User does not have permission to assign teams. " +
                            "Only Dispatchers and Security Managers can assign teams.");
        }

        // Validate incident can be assigned
        if (incident.getStatus() != IncidentStatus.NEW) {
            throw new BusinessRuleViolationException(
                    "Only NEW incidents can be assigned to a team. Current status: " + incident.getStatus());
        }

        // Get team
        IncidentTeam team = teamRepository.findById(assignmentDTO.getTeamId())
                .orElseThrow(() -> new BusinessRuleViolationException("Team not found"));

        // Validate team qualifications match incident type
        if (!team.getSpecialization().equals(incident.getType())) {
            throw new BusinessRuleViolationException(
                    "Team specialization (" + team.getSpecialization() + ") " +
                            "does not match incident type (" + incident.getType() + ")");
        }

        // Validate team availability
        if (team.getStatus() != TeamStatus.AVAILABLE) {
            throw new BusinessRuleViolationException("Team is not available. Current status: " + team.getStatus());
        }

        // Assign team
        incident.setAssignedTeam(team);
        incident.setStatus(IncidentStatus.ASSIGNED);

        // Update team status
        team.setStatus(TeamStatus.BUSY);
        teamRepository.save(team);

        // Save incident
        Incident updatedIncident = incidentRepository.save(incident);

        // Add log entry
        LogEntry logEntry = LogEntry.builder()
                .incident(updatedIncident)
                .employee(currentUser)
                .actionTime(LocalDateTime.now())
                .actionDescription("Team assigned")
                .details(String.format("Team: %s, Assignment notes: %s",
                        team.getTeamName(), assignmentDTO.getAssignmentNotes()))
                .build();
        logEntryRepository.save(logEntry);

        // Log audit
        auditLogService.logTeamAssignment(updatedIncident, team, currentUser);

        // Send notifications to team members
        notificationService.sendTeamAssignmentNotification(team, updatedIncident);

        // Check for escalation requirements
        checkForEscalation(updatedIncident);

        log.info("Team {} assigned to incident {}", team.getTeamName(), updatedIncident.getReportNumber());
        return convertToResponseDTO(updatedIncident, currentUser);
    }

    @Transactional
    public IncidentResponseDTO updateStatus(Long incidentId, StatusChangeDTO statusChangeDTO, Long currentUserId) {
        log.info("Updating status of incident {} to {} by user {}",
                incidentId, statusChangeDTO.getNewStatus(), currentUserId);

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentNotFoundException(incidentId));

        // Get current user
        Employee currentUser = employeeRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessRuleViolationException("User not found"));

        // Validate user can change status
        if (!canChangeStatus(currentUser, incident)) {
            throw new BusinessRuleViolationException(
                    "User does not have permission to change incident status.");
        }

        // Validate status transition
        if (!incident.canChangeStatus(statusChangeDTO.getNewStatus())) {
            throw new BusinessRuleViolationException(
                    String.format("Invalid status transition from %s to %s",
                            incident.getStatus(), statusChangeDTO.getNewStatus()));
        }

        IncidentStatus oldStatus = incident.getStatus();
        incident.setStatus(statusChangeDTO.getNewStatus());

        // Handle special status transitions
        if (statusChangeDTO.getNewStatus() == IncidentStatus.RESOLVED) {
            incident.setResolutionTime(LocalDateTime.now());

            // Auto-create basic closure report if team member is updating
            if (currentUser instanceof IncidentTeamMember) {
                createAutomaticClosureReport(incident, (IncidentTeamMember) currentUser,
                        statusChangeDTO.getActionNotes());
            }
        }

        // If closing incident
        if (statusChangeDTO.getNewStatus() == IncidentStatus.CLOSED) {
            // Validate closure report exists for non-OTHER incidents
            if (incident.getClosureReport() == null && incident.getType() != IncidentType.OTHER) {
                throw new BusinessRuleViolationException(
                        "Closure report is required before closing non-OTHER incidents");
            }
            incident.setClosureTime(LocalDateTime.now());
            incident.setReadOnly(true);

            // Release team
            if (incident.getAssignedTeam() != null) {
                IncidentTeam team = incident.getAssignedTeam();
                team.setStatus(TeamStatus.AVAILABLE);
                teamRepository.save(team);
            }
        }

        Incident updatedIncident = incidentRepository.save(incident);

        // Add log entry
        LogEntry logEntry = LogEntry.builder()
                .incident(updatedIncident)
                .employee(currentUser)
                .actionTime(LocalDateTime.now())
                .actionDescription("Status changed")
                .details(String.format("From %s to %s. Reason: %s",
                        oldStatus, statusChangeDTO.getNewStatus(),
                        statusChangeDTO.getChangeReason()))
                .build();
        logEntryRepository.save(logEntry);

        // Log audit
        auditLogService.logStatusChange(updatedIncident, oldStatus,
                statusChangeDTO.getNewStatus(), currentUser);

        // Send notifications if status is critical/high
        if (updatedIncident.getPriority() == IncidentPriority.CRITICAL ||
                updatedIncident.getPriority() == IncidentPriority.HIGH) {
            notificationService.sendStatusUpdateNotification(updatedIncident);
        }

        log.info("Incident {} status updated from {} to {}",
                updatedIncident.getReportNumber(), oldStatus, statusChangeDTO.getNewStatus());

        return convertToResponseDTO(updatedIncident, currentUser);
    }

    @Transactional
    public IncidentResponseDTO addClosureReport(Long incidentId, ClosureReportDTO reportDTO, Long currentUserId) {
        log.info("Adding closure report to incident {} by user {}", incidentId, currentUserId);

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentNotFoundException(incidentId));

        // Get current user
        Employee currentUser = employeeRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessRuleViolationException("User not found"));

        // Validate user can add closure report
        if (!canAddClosureReport(currentUser, incident)) {
            throw new BusinessRuleViolationException(
                    "User does not have permission to add closure reports.");
        }

        // Incident must be RESOLVED
        if (incident.getStatus() != IncidentStatus.RESOLVED) {
            throw new BusinessRuleViolationException(
                    "Incident must be RESOLVED before adding closure report. Current status: " + incident.getStatus());
        }

        // Create closure report
        ClosureReport closureReport = ClosureReport.builder()
                .incident(incident)
                .resolutionSummary(reportDTO.getResolutionSummary())
                .actionsTaken(reportDTO.getActionsTaken())
                .recommendations(reportDTO.getRecommendations())
                .creationTime(LocalDateTime.now())
                .createdBy(currentUser)
                .requiresManagerReview(reportDTO.isRequiresManagerReview())
                .build();

        // Save closure report
        ClosureReport savedReport = closureReportRepository.save(closureReport);
        incident.setClosureReport(savedReport);

        // Save incident
        Incident updatedIncident = incidentRepository.save(incident);

        // Add log entry
        LogEntry logEntry = LogEntry.builder()
                .incident(updatedIncident)
                .employee(currentUser)
                .actionTime(LocalDateTime.now())
                .actionDescription("Closure report added")
                .details("Closure report submitted" +
                        (reportDTO.isRequiresManagerReview() ? " (requires manager review)" : ""))
                .build();
        logEntryRepository.save(logEntry);

        // Log audit
        auditLogService.logClosureReport(updatedIncident, currentUser);

        // If requires manager review, send notification
        if (reportDTO.isRequiresManagerReview()) {
            notificationService.sendManagerReviewRequest(updatedIncident);
        }

        log.info("Closure report added to incident {}", updatedIncident.getReportNumber());
        return convertToResponseDTO(updatedIncident, currentUser);
    }

    public List<IncidentResponseDTO> getIncidents(IncidentStatus status, IncidentPriority priority,
                                                  IncidentType type, LocalDateTime from, LocalDateTime to,
                                                  Long userId) {
        log.debug("Getting incidents with filters: status={}, priority={}, type={}", status, priority, type);

        // Get current user for permission-based filtering
        Employee currentUser = userId != null ?
                employeeRepository.findById(userId).orElse(null) : null;

        List<Incident> incidents;

        if (status != null && priority != null) {
            incidents = incidentRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            incidents = incidentRepository.findByStatus(status);
        } else if (priority != null) {
            incidents = incidentRepository.findByPriority(priority);
        } else if (type != null) {
            incidents = incidentRepository.findByType(type);
        } else if (from != null && to != null) {
            incidents = incidentRepository.findIncidentsByDateRange(from, to);
        } else {
            incidents = incidentRepository.findAll();
        }

        // Filter based on user permissions
        if (currentUser != null && currentUser instanceof IncidentTeamMember) {
            // Team members only see incidents assigned to their team
            IncidentTeamMember member = (IncidentTeamMember) currentUser;
            incidents = incidents.stream()
                    .filter(incident -> incident.getAssignedTeam() != null &&
                            incident.getAssignedTeam().getMembers().contains(member))
                    .collect(Collectors.toList());
        }

        return incidents.stream()
                .map(incident -> convertToResponseDTO(incident, currentUser))
                .collect(Collectors.toList());
    }

    public IncidentResponseDTO getIncidentById(Long id, Long userId) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException(id));

        Employee currentUser = userId != null ?
                employeeRepository.findById(userId).orElse(null) : null;

        return convertToResponseDTO(incident, currentUser);
    }

    public List<IncidentResponseDTO> getActiveIncidents(Long userId) {
        List<IncidentStatus> activeStatuses = Arrays.asList(
                IncidentStatus.NEW,
                IncidentStatus.ASSIGNED,
                IncidentStatus.IN_PROGRESS
        );

        List<Incident> incidents = incidentRepository.findActiveIncidents(activeStatuses);

        Employee currentUser = userId != null ?
                employeeRepository.findById(userId).orElse(null) : null;

        // Filter based on user permissions
        if (currentUser != null && currentUser instanceof IncidentTeamMember) {
            IncidentTeamMember member = (IncidentTeamMember) currentUser;
            incidents = incidents.stream()
                    .filter(incident -> incident.getAssignedTeam() != null &&
                            incident.getAssignedTeam().getMembers().contains(member))
                    .collect(Collectors.toList());
        }

        return incidents.stream()
                .map(incident -> convertToResponseDTO(incident, currentUser))
                .collect(Collectors.toList());
    }

    public List<IncidentResponseDTO> getIncidentsForTeamMember(Long teamMemberId) {
        IncidentTeamMember member = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new BusinessRuleViolationException("Team member not found"));

        List<Incident> incidents = incidentRepository.findByAssignedTeamMembersContaining(member);

        return incidents.stream()
                .map(incident -> convertToResponseDTO(incident, member))
                .collect(Collectors.toList());
    }

    public List<AuditLog> getIncidentAuditLogs(Long incidentId, Long userId) {
        // Validate user has permission to view audit logs
        Employee currentUser = employeeRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleViolationException("User not found"));

        if (!(currentUser instanceof Dispatcher || currentUser instanceof SecurityManager)) {
            throw new BusinessRuleViolationException(
                    "Only Dispatchers and Security Managers can view audit logs");
        }

        return auditLogRepository.findByIncidentId(incidentId);
    }

    // ========== PRIVATE HELPER METHODS ==========

    private Dispatcher getDispatcherForIncident(Employee user) {
        if (user instanceof Dispatcher) {
            return (Dispatcher) user;
        } else if (user instanceof SecurityManager) {
            // Security Manager uses default dispatcher
            return dispatcherRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new BusinessRuleViolationException("No dispatcher available"));
        }
        throw new BusinessRuleViolationException("Cannot determine dispatcher for user");
    }

    private void validateIncidentCreation(IncidentDTO dto) {
        if (dto.getPriority() == IncidentPriority.CRITICAL &&
                (dto.getDescription() == null || dto.getDescription().length() < 20)) {
            throw new BusinessRuleViolationException(
                    "Critical incidents require detailed description (minimum 20 characters)");
        }

        if (dto.getLocationId() == null) {
            throw new BusinessRuleViolationException("Location is required");
        }

        if (dto.getType() == null) {
            throw new BusinessRuleViolationException("Incident type is required");
        }

        if (dto.getPriority() == null) {
            throw new BusinessRuleViolationException("Priority is required");
        }
    }

    private String generateReportNumber() {
        return "INC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() +
                "-" + LocalDateTime.now().getYear();
    }

    private IncidentPriority determinePriorityFromSensor(String alarmType) {
        return switch (alarmType.toUpperCase()) {
            case "SMOKE_DETECTED", "FIRE_ALARM", "EXPLOSION_DETECTED" -> IncidentPriority.CRITICAL;
            case "DOOR_FORCED", "UNAUTHORIZED_ACCESS", "INTRUSION_DETECTED" -> IncidentPriority.HIGH;
            case "WATER_LEAK", "POWER_OUTAGE", "EQUIPMENT_FAILURE" -> IncidentPriority.NORMAL;
            default -> IncidentPriority.LOW;
        };
    }

    private IncidentType determineTypeFromSensor(String alarmType) {
        return switch (alarmType.toUpperCase()) {
            case "SMOKE_DETECTED", "FIRE_ALARM", "EXPLOSION_DETECTED" -> IncidentType.FIRE;
            case "DOOR_FORCED", "UNAUTHORIZED_ACCESS", "INTRUSION_DETECTED" -> IncidentType.SECURITY_THREAT;
            case "WATER_LEAK", "POWER_OUTAGE" -> IncidentType.TECHNICAL;
            case "EQUIPMENT_FAILURE" -> IncidentType.EQUIPMENT;
            default -> IncidentType.OTHER;
        };
    }

    private void checkAffectedFlights(Incident incident) {
        try {
            if (incident.getLocation() != null && incident.getLocation().getCode() != null) {
                List<FlightDTO> affectedFlights = flightServiceClient.getFlightsByLocation(
                        incident.getLocation().getCode(), LocalDateTime.now());

                if (!affectedFlights.isEmpty()) {
                    LogEntry logEntry = LogEntry.builder()
                            .incident(incident)
                            .actionTime(LocalDateTime.now())
                            .actionDescription("Affected flights identified")
                            .details(String.format("Affects %d flight(s): %s",
                                    affectedFlights.size(),
                                    affectedFlights.stream()
                                            .map(FlightDTO::getFlightNumber)
                                            .collect(Collectors.joining(", "))))
                            .build();
                    logEntryRepository.save(logEntry);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to check affected flights for incident {}: {}",
                    incident.getReportNumber(), e.getMessage());
        }
    }

    private void blockFlightOperationsIfNeeded(Incident incident) {
        try {
            if (incident.getLocation() != null && incident.getLocation().getCode() != null) {
                flightServiceClient.blockOperationsInZone(
                        incident.getLocation().getCode(),
                        "CRITICAL_INCIDENT_" + incident.getType(),
                        incident.getId());

                LogEntry logEntry = LogEntry.builder()
                        .incident(incident)
                        .actionTime(LocalDateTime.now())
                        .actionDescription("Flight operations blocked")
                        .details("Flight operations in affected zone have been temporarily blocked")
                        .build();
                logEntryRepository.save(logEntry);
            }
        } catch (Exception e) {
            log.error("Failed to block flight operations for incident {}: {}",
                    incident.getReportNumber(), e.getMessage());
        }
    }

    private void autoAssignCriticalIncident(Incident incident) {
        try {
            // Find available team with matching specialization
            List<IncidentTeam> availableTeams = teamRepository.findByStatusAndSpecialization(
                    TeamStatus.AVAILABLE, incident.getType());

            if (!availableTeams.isEmpty()) {
                IncidentTeam team = availableTeams.get(0); // Take first available

                incident.setAssignedTeam(team);
                incident.setStatus(IncidentStatus.ASSIGNED);
                team.setStatus(TeamStatus.BUSY);

                incidentRepository.save(incident);
                teamRepository.save(team);

                LogEntry logEntry = LogEntry.builder()
                        .incident(incident)
                        .actionTime(LocalDateTime.now())
                        .actionDescription("Auto-assigned to team")
                        .details(String.format("Automatically assigned to team: %s (critical incident)",
                                team.getTeamName()))
                        .build();
                logEntryRepository.save(logEntry);

                notificationService.sendTeamAssignmentNotification(team, incident);

                log.info("Auto-assigned critical incident {} to team {}",
                        incident.getReportNumber(), team.getTeamName());
            }
        } catch (Exception e) {
            log.warn("Failed to auto-assign critical incident: {}", e.getMessage());
        }
    }

    private void createAutomaticClosureReport(Incident incident, IncidentTeamMember member, String notes) {
        if (incident.getClosureReport() == null) {
            ClosureReport autoReport = ClosureReport.builder()
                    .incident(incident)
                    .resolutionSummary("Incident resolved by team member")
                    .actionsTaken(notes != null ? notes : "Standard procedures followed")
                    .creationTime(LocalDateTime.now())
                    .createdBy(member)
                    .requiresManagerReview(incident.getPriority() == IncidentPriority.CRITICAL)
                    .build();

            closureReportRepository.save(autoReport);
            incident.setClosureReport(autoReport);
            incidentRepository.save(incident);

            log.info("Auto-created closure report for incident {} by team member {}",
                    incident.getReportNumber(), member.getId());
        }
    }

    private void checkForEscalation(Incident incident) {
        if (incident.getPriority() == IncidentPriority.HIGH ||
                incident.getPriority() == IncidentPriority.CRITICAL) {

            // Schedule escalation check in 10 minutes
            new Thread(() -> {
                try {
                    Thread.sleep(10 * 60 * 1000); // 10 minutes

                    Incident currentIncident = incidentRepository.findById(incident.getId())
                            .orElseThrow(() -> new IncidentNotFoundException(incident.getId()));

                    if (currentIncident.getStatus() == IncidentStatus.ASSIGNED) {
                        // Escalate to security manager
                        LogEntry escalationLog = LogEntry.builder()
                                .incident(currentIncident)
                                .actionTime(LocalDateTime.now())
                                .actionDescription("Incident escalated")
                                .details("Incident escalated to Security Manager due to lack of progress")
                                .build();
                        logEntryRepository.save(escalationLog);

                        notificationService.sendEscalationNotification(currentIncident);

                        log.warn("Incident {} escalated due to lack of progress",
                                currentIncident.getReportNumber());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error("Error in escalation check: {}", e.getMessage());
                }
            }).start();
        }
    }

    // ========== PERMISSION METHODS ==========

    private boolean canCreateIncident(Employee employee) {
        return employee instanceof Dispatcher ||
                employee instanceof SecurityManager;
    }

    private boolean canAssignTeam(Employee employee) {
        return employee instanceof Dispatcher ||
                employee instanceof SecurityManager;
    }

    private boolean canChangeStatus(Employee employee, Incident incident) {
        // Dispatcher and SecurityManager can change any status
        if (employee instanceof Dispatcher || employee instanceof SecurityManager) {
            return true;
        }

        // Team members can change status only for their assigned incidents
        if (employee instanceof IncidentTeamMember) {
            IncidentTeamMember member = (IncidentTeamMember) employee;

            // Check if member is part of the assigned team
            if (incident.getAssignedTeam() != null &&
                    incident.getAssignedTeam().getMembers().contains(member)) {

                // Team members can only change from ASSIGNED to IN_PROGRESS to RESOLVED
                return incident.getStatus() == IncidentStatus.ASSIGNED ||
                        incident.getStatus() == IncidentStatus.IN_PROGRESS ||
                        incident.getStatus() == IncidentStatus.RESOLVED;
            }
        }

        return false;
    }

    private boolean canAddClosureReport(Employee employee, Incident incident) {
        // SecurityManager can always add closure reports
        if (employee instanceof SecurityManager) {
            return true;
        }

        // Team members can add closure reports only for their assigned RESOLVED incidents
        if (employee instanceof IncidentTeamMember) {
            IncidentTeamMember member = (IncidentTeamMember) employee;

            return incident.getAssignedTeam() != null &&
                    incident.getAssignedTeam().getMembers().contains(member) &&
                    incident.getStatus() == IncidentStatus.RESOLVED;
        }

        return false;
    }

    private boolean canCloseIncident(Employee employee, Incident incident) {
        // Only Dispatcher and SecurityManager can close incidents
        return employee instanceof Dispatcher ||
                employee instanceof SecurityManager;
    }

    private boolean canEditIncident(Employee employee, Incident incident) {
        if (incident.isReadOnly()) {
            return false;
        }

        // Dispatcher and SecurityManager can edit
        if (employee instanceof Dispatcher || employee instanceof SecurityManager) {
            return true;
        }

        // Team members can only edit certain fields of their assigned incidents
        if (employee instanceof IncidentTeamMember) {
            IncidentTeamMember member = (IncidentTeamMember) employee;

            return incident.getAssignedTeam() != null &&
                    incident.getAssignedTeam().getMembers().contains(member) &&
                    (incident.getStatus() == IncidentStatus.ASSIGNED ||
                            incident.getStatus() == IncidentStatus.IN_PROGRESS);
        }

        return false;
    }

    private boolean isAssignedTeamMember(Employee employee, Incident incident) {
        if (employee instanceof IncidentTeamMember && incident.getAssignedTeam() != null) {
            IncidentTeamMember member = (IncidentTeamMember) employee;
            return incident.getAssignedTeam().getMembers().contains(member);
        }
        return false;
    }

    private String getUserRole(Employee employee) {
        if (employee instanceof Dispatcher) return "DISPATCHER";
        if (employee instanceof SecurityManager) return "SECURITY_MANAGER";
        if (employee instanceof IncidentTeamMember) return "TEAM_MEMBER";
        return "EMPLOYEE";
    }

    // ========== DTO CONVERSION ==========

    private IncidentResponseDTO convertToResponseDTO(Incident incident, Employee currentUser) {
        IncidentResponseDTO dto = new IncidentResponseDTO();

        // Basic fields
        dto.setId(incident.getId());
        dto.setReportNumber(incident.getReportNumber());
        dto.setType(incident.getType());
        dto.setPriority(incident.getPriority());
        dto.setStatus(incident.getStatus());
        dto.setReportSource(incident.getReportSource());
        dto.setDescription(incident.getDescription());
        dto.setCreationTime(incident.getCreationTime());
        dto.setClosureTime(incident.getClosureTime());

        // Relations
        if (incident.getLocation() != null) {
            dto.setLocation(convertToLocationDTO(incident.getLocation()));
        }

        if (incident.getRegisteredBy() != null) {
            dto.setRegisteredBy(convertToDispatcherDTO(incident.getRegisteredBy()));
        }

        if (incident.getAssignedTeam() != null) {
            dto.setAssignedTeam(convertToTeamDTO(incident.getAssignedTeam()));
        }

        if (incident.getProcedure() != null) {
            dto.setProcedure(convertToSOPDTO(incident.getProcedure()));
        }

        if (incident.getClosureReport() != null) {
            dto.setClosureReport(convertToClosureReportDTO(incident.getClosureReport()));
        }

        // Journal entries
        if (incident.getJournalEntries() != null) {
            dto.setJournalEntries(incident.getJournalEntries().stream()
                    .map(this::convertToLogEntryDTO)
                    .collect(Collectors.toList()));
        }

        // Affected resources
        if (incident.getAffectedResources() != null) {
            dto.setAffectedResources(incident.getAffectedResources().stream()
                    .map(this::convertToResourceDTO)
                    .collect(Collectors.toList()));
        }

        // Business flags based on current user
        if (currentUser != null) {
            dto.setUserRole(getUserRole(currentUser));
            dto.setCanEdit(canEditIncident(currentUser, incident));
            dto.setCanAssignTeam(canAssignTeam(currentUser));
            dto.setCanChangeStatus(canChangeStatus(currentUser, incident));
            dto.setCanAddClosureReport(canAddClosureReport(currentUser, incident));
            dto.setCanClose(canCloseIncident(currentUser, incident));
            dto.setIsAssignedTeamMember(isAssignedTeamMember(currentUser, incident));
        }

        // Allowed status transitions (filtered by user permissions)
        if (incident.getAllowedStatusTransitions() != null) {
            dto.setAllowedStatusTransitions(
                    incident.getAllowedStatusTransitions().stream()
                            .filter(status -> canChangeStatus(currentUser, incident) &&
                                    incident.canChangeStatus(status))
                            .collect(Collectors.toList())
            );
        }

        // Read-only flag
        dto.setReadOnly(incident.isReadOnly());

        return dto;
    }

    private LocationDTO convertToLocationDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setCode(location.getCode());
        dto.setType(location.getType());
        dto.setCoordinates(location.getCoordinates());
        return dto;
    }

    private DispatcherDTO convertToDispatcherDTO(Dispatcher dispatcher) {
        DispatcherDTO dto = new DispatcherDTO();
        dto.setId(dispatcher.getId());
        dto.setFirstName(dispatcher.getFirstName());
        dto.setLastName(dispatcher.getLastName());
        dto.setServiceNumber(dispatcher.getServiceNumber());
        dto.setIncidentsRegisteredCount(
                dispatcher.getRegisteredIncidents() != null ?
                        dispatcher.getRegisteredIncidents().size() : 0);
        return dto;
    }

    private IncidentTeamDTO convertToTeamDTO(IncidentTeam team) {
        IncidentTeamDTO dto = new IncidentTeamDTO();
        dto.setId(team.getId());
        dto.setTeamName(team.getTeamName());
        dto.setSpecialization(team.getSpecialization());
        dto.setStatus(team.getStatus());

        if (team.getMembers() != null) {
            dto.setMembers(team.getMembers().stream()
                    .map(this::convertToTeamMemberDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private IncidentTeamMemberDTO convertToTeamMemberDTO(IncidentTeamMember member) {
        IncidentTeamMemberDTO dto = new IncidentTeamMemberDTO();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setServiceNumber(member.getServiceNumber());
        dto.setRadioCallSign(member.getRadioCallSign());
        dto.setAvailable(member.isAvailable());
        return dto;
    }

    private StandardOperatingProcedureDTO convertToSOPDTO(StandardOperatingProcedure sop) {
        StandardOperatingProcedureDTO dto = new StandardOperatingProcedureDTO();
        dto.setId(sop.getId());
        dto.setProcedureName(sop.getProcedureName());
        dto.setDescription(sop.getDescription());
        dto.setIncidentType(sop.getIncidentType());
        return dto;
    }

    private ClosureReportDTO convertToClosureReportDTO(ClosureReport report) {
        ClosureReportDTO dto = new ClosureReportDTO();
        dto.setId(report.getId());
        dto.setResolutionSummary(report.getResolutionSummary());
        dto.setActionsTaken(report.getActionsTaken());
        dto.setRecommendations(report.getRecommendations());
        dto.setCreationTime(report.getCreationTime());
        dto.setRequiresManagerReview(report.isRequiresManagerReview());

        if (report.getReviewedBy() != null) {
            dto.setReviewedBy(convertToSecurityManagerDTO(report.getReviewedBy()));
            dto.setReviewTime(report.getReviewTime());
            dto.setReviewNotes(report.getReviewNotes());
        }

        return dto;
    }

    private SecurityManagerDTO convertToSecurityManagerDTO(SecurityManager manager) {
        SecurityManagerDTO dto = new SecurityManagerDTO();
        dto.setId(manager.getId());
        dto.setFirstName(manager.getFirstName());
        dto.setLastName(manager.getLastName());
        dto.setServiceNumber(manager.getServiceNumber());
        dto.setSecurityClearanceLevel(manager.getSecurityClearanceLevel());
        return dto;
    }

    private LogEntryDTO convertToLogEntryDTO(LogEntry logEntry) {
        LogEntryDTO dto = new LogEntryDTO();
        dto.setId(logEntry.getId());
        dto.setActionTime(logEntry.getActionTime());
        dto.setActionDescription(logEntry.getActionDescription());
        dto.setDetails(logEntry.getDetails());

        if (logEntry.getEmployee() != null) {
            dto.setEmployeeName(logEntry.getEmployee().getFirstName() + " " +
                    logEntry.getEmployee().getLastName());
            dto.setEmployeeServiceNumber(logEntry.getEmployee().getServiceNumber());
        }

        return dto;
    }

    private AirportResourceDTO convertToResourceDTO(AirportResource resource) {
        AirportResourceDTO dto = new AirportResourceDTO();
        dto.setId(resource.getId());
        dto.setResourceName(resource.getResourceName());
        dto.setResourceType(resource.getResourceType());
        dto.setStatus(resource.getStatus());
        dto.setLocation(resource.getLocation());
        return dto;
    }
}