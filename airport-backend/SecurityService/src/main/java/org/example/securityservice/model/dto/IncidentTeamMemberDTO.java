package org.example.securityservice.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.securityservice.model.entity.IncidentTeam;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentTeamMemberDTO extends EmployeeDTO {
    private String radioCallSign;
    private List<String> specializations;
    private IncidentTeam currentTeam;

    public IncidentTeamMemberDTO() {
        this.setEmployeeType("TEAM_MEMBER");
    }
}