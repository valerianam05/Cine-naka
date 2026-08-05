package com.example.demo.dto.response;

import java.util.List;
import java.util.UUID;

public record MovieResponse(
    UUID id, String title, List<String> genres, String description, long durationMinutes) {}
