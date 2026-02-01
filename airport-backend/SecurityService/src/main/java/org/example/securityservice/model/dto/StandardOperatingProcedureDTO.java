package org.example.securityservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.IncidentType;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardOperatingProcedureDTO {
    private Long id;
    private String procedureName;
    private String description;
    private List<ProcedureStepDTO> steps;
    private Set<IncidentType> applicableIncidentTypes;
}