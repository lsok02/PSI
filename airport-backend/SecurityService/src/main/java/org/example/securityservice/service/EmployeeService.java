package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee getEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }
}
