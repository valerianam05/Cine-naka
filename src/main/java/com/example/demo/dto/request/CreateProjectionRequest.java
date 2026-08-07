package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateProjectionRequest(
    @NotNull Instant dateTime,
    @NotNull @Positive BigDecimal seatPrice,
    @NotNull UUID movieId,
    @NotNull UUID roomId) {}
