package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_entries_seq")
    @SequenceGenerator(name = "log_entries_seq", sequenceName = "log_entries_seq", allocationSize = 1)
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