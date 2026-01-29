package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class LogEntry {

    @Id
    private Long id;
    private LocalDateTime actionTime;
    private String actionDescription;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee performedBy;

    @OneToMany(mappedBy = "logEntry")
    private List<Attachment> attachments;
}