package org.example.groundopsservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.example.groundopsservice.model.enumeration.TaskStatus;
import org.example.groundopsservice.model.enumeration.TaskType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime plannedStart;
    private LocalDateTime plannedEnd;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;

    // Relacja do zewnętrznego modułu Loty
    private Long flightId;

    @ManyToMany
    @JoinTable(
            name = "task_workers",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "worker_id")
    )
    private List<GroundWorker> assignedWorkers;

    @ManyToMany
    @JoinTable(
            name = "task_resources",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id")
    )
    private List<TechnicalResource> assignedResources;

    @OneToMany(mappedBy = "task")
    private List<IncidentReport> reportedIncidents;

    @ManyToOne
    @JoinColumn(name = "baggage_loading_id")
    private BaggageLoading baggageLoading;
}