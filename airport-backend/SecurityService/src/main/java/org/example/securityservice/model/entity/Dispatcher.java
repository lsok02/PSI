package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Dispatcher extends Employee {

    @OneToMany(mappedBy = "registeredBy")
    private List<Incident> registeredIncidents;
}