package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Shift {

    @Id
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}