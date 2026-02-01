package org.example.securityservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.exception.BusinessRuleViolationException;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.dto.SensorEventDTO;
import org.example.securityservice.model.entity.Dispatcher;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.Location;
import org.example.securityservice.model.entity.LogEntry;
import org.example.securityservice.model.entity.SensorEvent;
import org.example.securityservice.model.entity.StandardOperatingProcedure;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.ReportingSource;
import org.example.securityservice.model.mapper.IncidentMapper;
import org.example.securityservice.repository.DispatcherRepository;
import org.example.securityservice.repository.IncidentRepository;
import org.example.securityservice.repository.LocationRepository;
import org.example.securityservice.repository.LogEntryRepository;
import org.example.securityservice.repository.SensorEventRepository;
import org.example.securityservice.repository.StandardOperatingProcedureRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorIntegrationService {

    private final IncidentRepository incidentRepository;
    private final LocationRepository locationRepository;
    private final DispatcherRepository dispatcherRepository;
    private final SensorEventRepository sensorEventRepository;
    private final StandardOperatingProcedureRepository sopRepository;
    private final LogEntryRepository logEntryRepository;

    private final IncidentNotificationService notificationService;
    private final FlightIntegrationService flightIntegrationService;
    private final TeamAssignmentService teamAssignmentService;

    private final IncidentMapper incidentMapper;

    @Transactional
    public IncidentResponseDTO createIncidentFromSensor(SensorEventDTO sensorEventDTO) {
        log.info("Creating incident from sensor event: {}", sensorEventDTO.getAlarmType());

        IncidentPriority priority = determinePriorityFromSensor("defaultAlarm");
        IncidentType type = determineTypeFromSensor(String.valueOf(sensorEventDTO.getAlarmType()));

        SensorEvent event = createSensorEvent(sensorEventDTO);
        Location location = findLocationByZone(sensorEventDTO.getZoneCode());
        Dispatcher dispatcher = getDefaultDispatcher();

        Incident incident = buildIncidentFromSensor(type, priority, sensorEventDTO, location, dispatcher);
        incident.setSop(findSopForIncidentType(type));

        Incident savedIncident = incidentRepository.save(incident);
        linkSensorEventToIncident(event, savedIncident);
        updateDispatcherIncidents(dispatcher, savedIncident);
        createSensorLogEntry(savedIncident, sensorEventDTO);

        handleCriticalSensorIncident(savedIncident, sensorEventDTO, priority);
        notificationService.logSensorIncidentCreation(savedIncident);

        log.info("Sensor incident created: {}", savedIncident.getReportNumber());
        return incidentMapper.toResponseDto(savedIncident);
    }

    private SensorEvent createSensorEvent(SensorEventDTO dto) {
        return SensorEvent.builder()
                .sensorId(dto.getSensorId())
                .sensorType(dto.getAlarmType())
                .processedAt(LocalDateTime.now().minusSeconds(30))
                .isProcessed(true)
                .build();
    }

    private Dispatcher getDefaultDispatcher() {
        return dispatcherRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new BusinessRuleViolationException("No dispatcher available"));
    }

    private Incident buildIncidentFromSensor(IncidentType type, IncidentPriority priority,
                                             SensorEventDTO sensorEvent, Location location,
                                             Dispatcher dispatcher) {
        return Incident.builder()
                .reportNumber(generateReportNumber())
                .type(type)
                .priority(priority)
                .status(IncidentStatus.NEW)
                .source(ReportingSource.SYSTEM)
                .reportTime(LocalDateTime.now())
                .description(String.format("Sensor alert: %s in zone %s (%s)",
                        sensorEvent.getAlarmType(), sensorEvent.getZoneCode(), location.getName()))
                .registeredBy(dispatcher)
                .location(location)
                .build();
    }

    private StandardOperatingProcedure findSopForIncidentType(IncidentType type) {
        return sopRepository.findByApplicableIncidentTypesContaining(type)
                .stream()
                .findFirst()
                .orElse(null);
    }

    private void linkSensorEventToIncident(SensorEvent event, Incident incident) {
        event.setIncident(incident);
        sensorEventRepository.save(event);
    }

    private void updateDispatcherIncidents(Dispatcher dispatcher, Incident incident) {
        if (dispatcher.getRegisteredIncidents() == null) {
            dispatcher.setRegisteredIncidents(new ArrayList<>());
        }
        dispatcher.getRegisteredIncidents().add(incident);
    }

    private void createSensorLogEntry(Incident incident, SensorEventDTO sensorEvent) {
        LogEntry logEntry = LogEntry.builder()
                .incident(incident)
                .actionTime(LocalDateTime.now())
                .actionDescription("Incident created from sensor event")
                .build();
        logEntryRepository.save(logEntry);
    }

    private void handleCriticalSensorIncident(Incident incident, SensorEventDTO sensorEvent, IncidentPriority priority) {
        if (priority == IncidentPriority.CRITICAL) {

//            if (sensorEvent.getAlarmType().contains("FIRE") ||
//                    sensorEvent.getAlarmType().contains("SMOKE")) {
                flightIntegrationService.blockFlightOperationsIfNeeded(incident);
//            }

            teamAssignmentService.autoAssignCriticalIncident(incident);
        }
    }

    private String generateReportNumber() {
        return "SENSOR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() +
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

    private Location findLocationByZone(String zoneCode) {
        return locationRepository.findLocationByName(zoneCode)
                .orElseThrow(() -> new BusinessRuleViolationException("Zone not found: " + zoneCode));
    }
}