package org.example.securityservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProcedureStepDTO {
    private Long id;
    private int stepNumber;
    private String title;
    private String description;
    private String expectedDuration;
    private List<String> requiredResources;
}