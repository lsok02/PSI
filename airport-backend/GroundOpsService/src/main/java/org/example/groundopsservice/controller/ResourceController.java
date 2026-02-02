package org.example.groundopsservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.groundopsservice.model.dto.ResourceSummaryDTO;
import org.example.groundopsservice.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groundops/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<ResourceSummaryDTO>> getResources() {
        return ResponseEntity.ok(resourceService.getResources());
    }
}
