package org.example.securityservice.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String downloadUrl;
    private LocalDateTime uploadTime;
}