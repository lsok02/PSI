package org.example.securityservice.repository;

import org.example.securityservice.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByIncidentId(Long incidentId);

    List<AuditLog> findByEmployeeId(Long employeeId);

    List<AuditLog> findByActionType(String actionType);
}