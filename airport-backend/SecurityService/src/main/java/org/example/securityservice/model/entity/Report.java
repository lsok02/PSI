package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Report {

    @Id
    private Long id;
    private LocalDateTime generatedAt;
    private String reportType;
    private String dateRange;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private SecurityManager generatedBy;

    @ManyToMany
    private List<Incident> analyzedIncidents;
}