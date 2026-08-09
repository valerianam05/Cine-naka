package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.request.CreateMovieRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.service.MovieService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @PutMapping
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<MovieResponse> createOrUpdateMovie(
      @Valid @RequestBody CreateMovieRequest request) {
    MovieResponse response = movieService.createOrUpdateMovie(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MovieResponse> getMovieById(@PathVariable UUID id) {
    return ResponseEntity.ok(movieService.getMovieById(id));
  }
}
