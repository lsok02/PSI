package org.example.securityservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.ApprovalStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "closure_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosureReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    @Lob
    private String summary;

    @Lob
    private String rootCauseAnalysis;

    @Lob
    private String actionsTaken;

    @Lob
    private String recommendations;

    @ElementCollection
    private List<String> attachments; // ścieżki do plików

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private Employee approvedBy;

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus; // PENDING, APPROVED, REJECTED

    private String rejectionReason;

}