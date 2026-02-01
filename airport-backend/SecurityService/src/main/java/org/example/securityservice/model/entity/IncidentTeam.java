package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.TeamStatus;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentTeam {

    @Id
    private Long id;
    private String teamName;

    @Enumerated(EnumType.STRING)
    private IncidentType specialization;

    @Enumerated(EnumType.STRING)
    private TeamStatus status;

}