package org.example.securityservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportResourceDTO {
    private Long id;
    private String resourceName;
    private String resourceType;
}