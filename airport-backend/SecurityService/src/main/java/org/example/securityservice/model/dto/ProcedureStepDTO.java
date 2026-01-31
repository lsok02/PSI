package org.example.securityservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProcedureStepDTO {
    private Long id;
    private int stepNumber;
    private String description;
}