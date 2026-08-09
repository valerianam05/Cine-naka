package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreateRoomRequest(
    @NotBlank String number, @NotNull @Positive int capacity, List<CreateSeatRequest> seats) {}
