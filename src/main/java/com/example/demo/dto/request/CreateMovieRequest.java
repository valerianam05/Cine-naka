package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateMovieRequest(
    @NotBlank String title,
    @Size(min = 1) List<String> genres,
    @NotBlank String description,
    @NotNull @Positive long durationMinutes) {}
