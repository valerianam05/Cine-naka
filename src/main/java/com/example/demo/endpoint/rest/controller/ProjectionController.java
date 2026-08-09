package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.request.CreateProjectionRequest;
import com.example.demo.dto.response.ProjectionResponse;
import com.example.demo.service.ProjectionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projections")
@RequiredArgsConstructor
public class ProjectionController {

  private final ProjectionService projectionService;

  @GetMapping
  public ResponseEntity<List<ProjectionResponse>> getAllProjections() {
    return ResponseEntity.ok(projectionService.getAllProjections());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectionResponse> getProjectionById(@PathVariable UUID id) {
    return ResponseEntity.ok(projectionService.getProjectionById(id));
  }

  @PutMapping("/projection")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<ProjectionResponse> createOrUpdateProjection(
      @Valid @RequestBody CreateProjectionRequest request) {
    ProjectionResponse response = projectionService.createOrUpdateProjection(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
