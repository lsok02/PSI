package org.example.securityservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntryDTO {
    private Long id;
    private LocalDateTime actionTime;
    private String actionDescription;

    // Zamiast pełnych obiektów, tylko ID lub podstawowe info
    private Long incidentId;
    private EmployeeDTO performedBy;
    private List<AttachmentDTO> attachments;
}