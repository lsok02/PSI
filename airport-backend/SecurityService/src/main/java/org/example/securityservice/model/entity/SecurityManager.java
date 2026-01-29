package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityManager extends Employee {

    @OneToMany(mappedBy = "escalatedTo")
    private List<Incident> escalatedIncidents;
}