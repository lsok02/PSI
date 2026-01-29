package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    private Long id;
    private String name;
    private String type;
    private String coordinates;
}