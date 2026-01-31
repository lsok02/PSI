package org.example.securityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IncidentNotFoundException extends RuntimeException {

    private final Long incidentId;
    private final String reportNumber;

    public IncidentNotFoundException(Long incidentId) {
        super(String.format("Incident with ID %d not found", incidentId));
        this.incidentId = incidentId;
        this.reportNumber = null;
    }

    public IncidentNotFoundException(String reportNumber) {
        super(String.format("Incident with report number %s not found", reportNumber));
        this.incidentId = null;
        this.reportNumber = reportNumber;
    }

    public IncidentNotFoundException(Long incidentId, String message) {
        super(message);
        this.incidentId = incidentId;
        this.reportNumber = null;
    }

    public IncidentNotFoundException(String reportNumber, String message) {
        super(message);
        this.incidentId = null;
        this.reportNumber = reportNumber;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    // Helper method to check if we have an ID or report number
    public boolean hasIncidentId() {
        return incidentId != null;
    }

    public boolean hasReportNumber() {
        return reportNumber != null;
    }
}