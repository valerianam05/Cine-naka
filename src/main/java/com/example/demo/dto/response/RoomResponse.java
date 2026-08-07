package com.example.demo.dto.response;

import java.util.List;
import java.util.UUID;

public record RoomResponse(UUID id, String number, int capacity, List<SeatResponse> seats) {}
