package org.example.groundopsservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.example.groundopsservice.model.enumeration.FailureStatus;
import org.example.groundopsservice.model.enumeration.FailureType;
import org.example.groundopsservice.model.enumeration.FailureUrgency;

import java.time.LocalDateTime;

@Entity
@Data
public class FailureReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private TechnicalResource resource;

    @Enumerated(EnumType.STRING)
    private FailureType failureType;

    private String description;

    @Enumerated(EnumType.STRING)
    private FailureUrgency urgency;

    private String location;

    private LocalDateTime reportedAt;

    @Enumerated(EnumType.STRING)
    private FailureStatus status;

    private String reportedBy;

    private Long securityIncidentId;
}
