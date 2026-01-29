package org.example.groundopsservice.model.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.groundopsservice.model.enumeration.AvailabilityStatus;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroundWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String role;

    @ElementCollection
    private List<String> qualifications;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @ManyToMany(mappedBy = "assignedWorkers")
    private List<Task> tasks;
}