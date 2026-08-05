package com.example.demo.dto.response;

import java.util.UUID;

public record SeatResponse(UUID id, String number, boolean available) {}
