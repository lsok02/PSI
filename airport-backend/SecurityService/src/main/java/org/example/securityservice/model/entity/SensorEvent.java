package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.SensorType;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;

    @Enumerated(EnumType.STRING)
    private SensorType sensorType; // FIRE, SMOKE, MOTION, TEMPERATURE, etc.

    private String locationDetails;
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id")
    private Incident incident;

    private Boolean isProcessed = false;
}