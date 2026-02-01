package org.example.securityservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.TeamStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentTeamDTO {
    private Long id;
    private String teamName;
    private String specialization;
    private TeamStatus status;
    private List<IncidentTeamMemberDTO> members;

}