package org.example.securityservice.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentTeamMemberDTO extends EmployeeDTO {
    private String radioCallSign;
    private List<String> specializations;
    private IncidentTeamDTO currentTeam;
    private boolean available;

    public IncidentTeamMemberDTO() {
        this.setEmployeeType("TEAM_MEMBER");
    }
}