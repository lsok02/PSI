package org.example.groundopsservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.groundopsservice.model.dto.FailureReportRequest;
import org.example.groundopsservice.model.dto.FailureReportResponse;
import org.example.groundopsservice.service.FailureReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groundops/failures")
@RequiredArgsConstructor
public class FailureReportController {

    private final FailureReportService failureReportService;

    @PostMapping
    public ResponseEntity<FailureReportResponse> createFailure(
            @RequestBody FailureReportRequest request,
            @RequestHeader(value = "token", required = false) String token) {
        FailureReportResponse response = failureReportService.reportFailure(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FailureReportResponse>> getFailures() {
        return ResponseEntity.ok(failureReportService.getFailures());
    }
}
