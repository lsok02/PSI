package org.example.securityservice.model.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class ClosureReportDTO {

    @NotNull
    private String resolutionSummary;

    private String actionsTaken;

    private String recommendations;

    private boolean requiresManagerReview;
}