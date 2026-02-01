package org.example.securityservice.model.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class TeamAssignmentDTO {

    @NotNull
    private Long teamId;

    private String assignmentNotes;
}