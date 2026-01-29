package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.example.securityservice.model.enumeration.AttendanceStatus;

import java.time.LocalDateTime;

@Entity
@Data
public class ShiftAssignment {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private LocalDateTime actualCheckIn;
    private LocalDateTime actualCheckOut;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;
}