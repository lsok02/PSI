package org.example.groundopsservice.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Vehicle extends TechnicalResource {
    private String registrationNumber;
    private String vehicleType;
}