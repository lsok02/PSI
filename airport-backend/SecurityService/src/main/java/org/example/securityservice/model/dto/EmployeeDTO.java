package org.example.securityservice.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "employeeType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DispatcherDTO.class, name = "DISPATCHER"),
        @JsonSubTypes.Type(value = SecurityManagerDTO.class, name = "SECURITY_MANAGER"),
        @JsonSubTypes.Type(value = IncidentTeamMemberDTO.class, name = "TEAM_MEMBER"),
        @JsonSubTypes.Type(value = EmployeeDTO.class, name = "EMPLOYEE") // bazowy
})
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String serviceNumber;


    private String employeeType;
}
