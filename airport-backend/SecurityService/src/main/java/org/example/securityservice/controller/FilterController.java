package org.example.securityservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.securityservice.service.FilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/filters")
@RequiredArgsConstructor
public class FilterController {

    private final FilterService filterService;
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFilters() {
        Map<String, Object> response = new HashMap<>();
        response.put("statuses", filterService.getAllStatuses());
        response.put("types", filterService.getAllTypes());
        response.put("priorities", filterService.getAllPriorities());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/statuses")
    public ResponseEntity<Map<String, Object>> getStatuses() {
        Map<String, Object> response = new HashMap<>();
        response.put("statuses", filterService.getAllStatuses());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getTypes() {
        Map<String, Object> response = new HashMap<>();
        response.put("types", filterService.getAllTypes());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/priorities")
    public ResponseEntity<Map<String, Object>> getPriorities() {
        Map<String, Object> response = new HashMap<>();
        response.put("priorities", filterService.getAllPriorities());

        return ResponseEntity.ok(response);
    }
}