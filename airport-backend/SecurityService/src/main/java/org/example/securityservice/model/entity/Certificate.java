package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateName; // np. "Advanced First Aid", "Fire Safety Level 2"

    private LocalDateTime expiryDate; // data ważności z diagramu

    @ManyToOne
    @JoinColumn(name = "member_id")
    private IncidentTeamMember member; // powiązanie z konkretnym członkiem zespołu
}