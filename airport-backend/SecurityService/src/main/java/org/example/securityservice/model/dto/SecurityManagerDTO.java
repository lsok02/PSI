package org.example.securityservice.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityManagerDTO extends EmployeeDTO {
    private List<String> certifications;
    private String securityClearanceLevel;
    private boolean onCall;

    public SecurityManagerDTO() {
        this.setEmployeeType("SECURITY_MANAGER");
    }
}
