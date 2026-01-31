package org.example.securityservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.IncidentType;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardOperatingProcedure {

    @Id
    private Long id;
    private String procedureName;
    private String description;

    @OneToMany(mappedBy = "sop", cascade = CascadeType.ALL)
    private List<ProcedureStep> steps;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<IncidentType> applicableIncidentTypes;
}