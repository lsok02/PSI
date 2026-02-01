package org.example.securityservice.model.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.example.securityservice.model.enumeration.IncidentStatus;

@Data
public class StatusChangeDTO {

    @NotNull
    private IncidentStatus newStatus;

    private String changeReason;

    private String actionNotes;
}