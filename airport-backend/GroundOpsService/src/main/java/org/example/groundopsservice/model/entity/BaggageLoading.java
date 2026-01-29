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
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.example.groundopsservice.model.enumeration.LoadingStatus;

import java.util.List;

@Entity
@Data
public class BaggageLoading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flightId; // FK do modułu Loty

    @Enumerated(EnumType.STRING)
    private LoadingStatus status;

    private Integer plannedBaggageCount;
    private Integer actualBaggageCount;

    @ManyToMany
    @JoinTable(
            name = "loading_workers",
            joinColumns = @JoinColumn(name = "loading_id"),
            inverseJoinColumns = @JoinColumn(name = "worker_id")
    )
    private List<GroundWorker> assignedWorkers;

    @OneToMany(mappedBy = "baggageLoading")
    private List<Task> relatedTasks;
}