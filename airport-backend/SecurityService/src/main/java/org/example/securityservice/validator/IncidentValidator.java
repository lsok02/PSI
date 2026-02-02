package org.example.securityservice.validator;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.exception.BusinessRuleViolationException;
import org.example.securityservice.exception.IncidentNotFoundException;
import org.example.securityservice.model.dto.IncidentDTO;
import org.example.securityservice.model.entity.Dispatcher;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.SecurityManager;
import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.entity.IncidentTeamMember;
import org.example.securityservice.model.entity.Location;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.repository.EmployeeRepository;
import org.example.securityservice.repository.IncidentRepository;
import org.example.securityservice.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncidentValidator {

    private final EmployeeRepository employeeRepository;
    private final IncidentRepository incidentRepository;
    private final LocationRepository locationRepository;

    public Employee validateAndGetUser(Long userId) {
        return employeeRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleViolationException("User not found"));
    }

    public Employee getUserIfExists(Long userId) {
        return employeeRepository.findById(userId).orElse(null);
    }

    public Incident validateAndGetIncident(Long incidentId) {
        return incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentNotFoundException(incidentId));
    }

    public Location validateLocationExists(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new BusinessRuleViolationException("Location not found"));
    }

    public void validateUserCanCreateIncident(Employee employee) {
        if (!(employee instanceof Dispatcher || employee instanceof SecurityManager)) {
            throw new BusinessRuleViolationException(
                    "User does not have permission to create incidents. " +
                            "Only Dispatchers and Security Managers can create incidents.");
        }
    }

    public void validateIncidentCreation(IncidentDTO dto) {
        if (dto.getPriority() == IncidentPriority.CRITICAL &&
                (dto.getDescription() == null || dto.getDescription().length() < 20)) {
            throw new BusinessRuleViolationException(
                    "Critical incidents require detailed description (minimum 20 characters)");
        }

        if (dto.getLocationId() == null) {
            throw new BusinessRuleViolationException("Location is required");
        }

        if (dto.getType() == null) {
            throw new BusinessRuleViolationException("Incident type is required");
        }

        if (dto.getPriority() == null) {
            throw new BusinessRuleViolationException("Priority is required");
        }

        if (dto.getReportSource() == null) {
            throw new BusinessRuleViolationException("Report source is required");
        }

        // Walidacja czy lokalizacja istnieje
        validateLocationExists(dto.getLocationId());
    }

    public void validateUserCanAssignTeam(Employee employee) {
        if (!(employee instanceof Dispatcher || employee instanceof SecurityManager)) {
            throw new BusinessRuleViolationException(
                    "User does not have permission to assign teams. " +
                            "Only Dispatchers and Security Managers can assign teams.");
        }
    }

    public void validateIncidentCanBeAssigned(Incident incident) {
        if (incident.getStatus() != IncidentStatus.NEW) {
            throw new BusinessRuleViolationException(
                    "Only NEW incidents can be assigned to a team. Current status: " + incident.getStatus());
        }
    }

    public void validateUserCanChangeStatus(Employee employee, Incident incident) {
        // Dispatcher and SecurityManager can change any status
        if (employee instanceof Dispatcher || employee instanceof SecurityManager) {
            return;
        }

        // Team members can change status only for their assigned incidents
        if (employee instanceof IncidentTeamMember) {
            IncidentTeamMember member = (IncidentTeamMember) employee;

//            if (incident.getAssignedTeam() != null &&
//                    incident.getAssignedTeam().getMembers().contains(member)) {

            // Team members can only change from ASSIGNED to IN_PROGRESS to RESOLVED
            if (incident.getStatus() == IncidentStatus.ASSIGNED ||
                    incident.getStatus() == IncidentStatus.IN_PROGRESS ||
                    incident.getStatus() == IncidentStatus.RESOLVED) {
                return;
            }
//            }
        }

        throw new BusinessRuleViolationException(
                "User does not have permission to change incident status.");
    }

    public void validateIncidentIsResolved(Incident incident) {
        if (incident.getStatus() != IncidentStatus.RESOLVED) {
            throw new BusinessRuleViolationException(
                    "Incident must be RESOLVED before adding closure report. Current status: " + incident.getStatus());
        }
    }

}
