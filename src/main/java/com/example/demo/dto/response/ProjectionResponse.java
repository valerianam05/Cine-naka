package com.example.demo.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProjectionResponse(
    UUID id, Instant dateTime, BigDecimal seatPrice, MovieSummary movie, RoomSummary room) {}

record MovieSummary(UUID id, String title) {}

record RoomSummary(UUID id, String number) {}
