package org.example.securityservice.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DispatcherDTO extends EmployeeDTO {
    private int incidentsRegisteredCount;

    public DispatcherDTO() {
        this.setEmployeeType("DISPATCHER");
    }
}