package org.example.securityservice.service;

import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterService {

    public List<String> getAllStatuses() {
        return Arrays.stream(IncidentStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<String> getAllTypes() {
        return Arrays.stream(IncidentType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<String> getAllPriorities() {
        return Arrays.stream(IncidentPriority.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}