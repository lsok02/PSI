package org.example.groundopsservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.example.groundopsservice.model.enumeration.ResourceStatus;
import org.example.groundopsservice.model.enumeration.ResourceType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TechnicalResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status;

    private LocalDateTime nextMaintenanceDate;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @OneToMany(mappedBy = "relatedResource")
    private List<IncidentReport> incidents;
}