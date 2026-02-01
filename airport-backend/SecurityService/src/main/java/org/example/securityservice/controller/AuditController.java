package org.example.securityservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.entity.AuditLog;
import org.example.securityservice.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogService auditLogService;

    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<AuditLog>> getIncidentAuditLogs(@PathVariable Long incidentId) {
        List<AuditLog> logs = auditLogService.getAuditLogsByIncident(incidentId);
        return ResponseEntity.ok(logs);
    }
}