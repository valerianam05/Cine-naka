package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSeatRequest(@NotBlank String number, boolean available) {}
