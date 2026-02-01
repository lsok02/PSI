package org.example.securityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.securityservice.model.dto.SensorEventDTO;
import org.example.securityservice.model.entity.SensorEvent;
import org.example.securityservice.service.SensorEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-events")
@RequiredArgsConstructor
@Slf4j
public class SensorEventController {

    private final SensorEventService sensorEventService;

    @GetMapping("/unassigned")
    public ResponseEntity<List<SensorEventDTO>> getUnassignedEvents() {
        log.info("Fetching unassigned sensor events");
        List<SensorEventDTO> events = sensorEventService.getUnassignedSensorEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/random-alarm")
    public ResponseEntity<SensorEvent> createRandomAlarm() {
        log.info("Creating random sensor alarm");
        SensorEvent createdEvent = sensorEventService.createRandomAlarm();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PostMapping("/simulate-alarms")
    public ResponseEntity<List<SensorEvent>> createSimulatedAlarms(
            @RequestParam(defaultValue = "5") int count) {
        log.info("Creating {} simulated sensor alarms", count);
        List<SensorEvent> createdEvents = sensorEventService.createSimulatedAlarms(count);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvents);
    }

}