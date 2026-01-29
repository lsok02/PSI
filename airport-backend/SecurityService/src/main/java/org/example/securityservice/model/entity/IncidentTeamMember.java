package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentTeamMember extends Employee {
    private String radioId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private IncidentTeam team;

    @OneToMany(mappedBy = "member")
    private List<Certificate> certificates;
}