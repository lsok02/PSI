package org.example.groundopsservice.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SpecializedEquipment extends TechnicalResource {
    private String category;
}