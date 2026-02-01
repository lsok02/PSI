package org.example.securityservice.model.mapper;

import org.example.securityservice.model.dto.EmployeeDTO;
import org.example.securityservice.model.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements BaseMapper<Employee, EmployeeDTO> {

    @Override
    public EmployeeDTO toDto(Employee entity) {
        if (entity == null) return null;

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setServiceNumber(entity.getServiceNumber());
        return dto;
    }

    @Override
    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) return null;

        return Employee.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .serviceNumber(dto.getServiceNumber())
                .build();
    }
}