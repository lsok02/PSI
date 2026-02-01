package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.TeamStatus;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentTeam {

    @Id
    private Long id;
    private String teamName;
    private String specialization;

    @Enumerated(EnumType.STRING)
    private TeamStatus status;

//    @OneToMany(mappedBy = "team")
//    private List<IncidentTeamMember> members;
}