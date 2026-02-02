package org.example.securityservice.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.IncidentResponseDTO;
import org.example.securityservice.model.dto.SensorEventDTO;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.Location;
import org.example.securityservice.model.entity.SensorEvent;
import org.example.securityservice.model.enumeration.SensorType;
import org.example.securityservice.repository.IncidentRepository;
import org.example.securityservice.repository.LocationRepository;
import org.example.securityservice.repository.SensorEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorEventService {

    private final SensorEventRepository sensorEventRepository;
    private final LocationRepository locationRepository;
    private final IncidentRepository incidentRepository;
    private final Random random = new Random();

    public List<SensorEventDTO> getUnassignedSensorEvents() {
        List<SensorEvent> events = sensorEventRepository.findByIncidentIsNull();
        return events.stream()
                .map(SensorEventDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void addIncidentForAlarm(Long alarmId, IncidentResponseDTO createdIncident) {
        SensorEvent event = sensorEventRepository.getSensorEventById(alarmId);
        Optional<Incident> incident = incidentRepository.findById(createdIncident.getId());
        event.setIncident(incident.get());
        sensorEventRepository.save(event);
    }

    public SensorEvent createRandomAlarm() {
        try {
            SensorType[] sensorTypes = SensorType.values();
            SensorType randomType = sensorTypes[random.nextInt(sensorTypes.length)];

            List<Location> locations = locationRepository.findAll();
            if (locations.isEmpty()) {
                throw new IllegalStateException("No locations available in database");
            }

            Location randomLocation = locations.get(random.nextInt(locations.size()));
            String sensorId = "RANDOM-" + generateRandomSensorId();
            String locationDetails = generateLocationDetails(randomLocation, randomType);

            SensorEvent sensorEvent = SensorEvent.builder()
                    .sensorId(sensorId)
                    .sensorType(randomType)
                    .locationDetails(locationDetails)
                    .timestamp(LocalDateTime.now())
                    .location(randomLocation)
                    .incident(null) // Celowo nie przypisany do incydentu
                    .isProcessed(false)
                    .build();

            SensorEvent savedEvent = sensorEventRepository.save(sensorEvent);

            log.info("Created random sensor alarm: type={}, location={}, sensorId={}",
                    randomType, randomLocation.getName(), sensorId);

            return savedEvent;

        } catch (Exception e) {
            log.error("Error creating random sensor alarm", e);
            throw new RuntimeException("Failed to create random sensor alarm", e);
        }
    }

    @Scheduled(fixedDelay = 4 * 60 * 1000) // 5 minut
    public void scheduleRandomAlarms() {
        log.debug("Scheduled task: Creating random sensor alarm");
        try {
            createRandomAlarm();
        } catch (Exception e) {
            log.error("Error in scheduled random alarm creation", e);
        }
    }

    private String generateRandomSensorId() {
        String[] prefixes = {"SENSOR", "DETECTOR", "MONITOR", "ALARM"};
        String prefix = prefixes[random.nextInt(prefixes.length)];
        int number = 1000 + random.nextInt(9000); // 1000-9999
        char letter = (char) ('A' + random.nextInt(26)); // A-Z

        return prefix + "-" + number + letter;
    }

    private String generateLocationDetails(Location location, SensorType sensorType) {
        String[] detailsTemplates = {
                "{} detected at {}",
                "{} alert triggered in {}",
                "{} reading abnormal at {}",
                "{} sensor activated in {}"
        };

        String template = detailsTemplates[random.nextInt(detailsTemplates.length)];
        String sensorTypeDescription = getSensorTypeDescription(sensorType);

        return template.replace("{}", sensorTypeDescription)
                .replace("{}", location.getName());
    }

    private String getSensorTypeDescription(SensorType sensorType) {
        return switch (sensorType) {
            case FIRE -> "Fire";
            case SMOKE -> "Smoke";
            case MOTION -> "Motion";
            case TEMPERATURE -> "Temperature anomaly";
            case PRESSURE -> "Pressure change";
            case HUMIDITY -> "Humidity variation";
            case CAMERA -> "Camera alert";
            case ACCESS_CONTROL -> "Access violation";
            case VIBRATION -> "Vibration detected";
        };
    }

    @Transactional
    public List<SensorEvent> createSimulatedAlarms(int count) {
        List<SensorEvent> alarms = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                alarms.add(createRandomAlarm());
            } catch (Exception e) {
                log.error("Error creating simulated alarm {} of {}", i + 1, count, e);
            }
        }
        return alarms;
    }
}