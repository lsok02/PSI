package org.example.securityservice.service;

import org.example.securityservice.model.entity.Incident;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class IncidentSpecifications {

    public static Specification<Incident> hasStatus(IncidentStatus status) {
        return (root, query, criteriaBuilder) ->
                status != null ? criteriaBuilder.equal(root.get("status"), status) : null;
    }

    public static Specification<Incident> hasPriority(IncidentPriority priority) {
        return (root, query, criteriaBuilder) ->
                priority != null ? criteriaBuilder.equal(root.get("priority"), priority) : null;
    }

    public static Specification<Incident> hasType(IncidentType type) {
        return (root, query, criteriaBuilder) ->
                type != null ? criteriaBuilder.equal(root.get("type"), type) : null;
    }

    public static Specification<Incident> reportedAfter(LocalDateTime from) {
        return (root, query, criteriaBuilder) ->
                from != null ? criteriaBuilder.greaterThanOrEqualTo(root.get("reportTime"), from) : null;
    }

    public static Specification<Incident> reportedBefore(LocalDateTime to) {
        return (root, query, criteriaBuilder) ->
                to != null ? criteriaBuilder.lessThanOrEqualTo(root.get("reportTime"), to) : null;
    }
}