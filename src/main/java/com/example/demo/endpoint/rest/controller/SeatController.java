package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.response.SeatResponse;
import com.example.demo.service.SeatService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

  private final SeatService seatService;

  @GetMapping("/room/{roomId}")
  public ResponseEntity<List<SeatResponse>> getSeatsByRoom(@PathVariable UUID roomId) {
    return ResponseEntity.ok(seatService.getSeatsByRoom(roomId));
  }

  @PatchMapping("/{id}/availability")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<SeatResponse> updateSeatAvailability(
      @PathVariable UUID id, @RequestParam boolean available) {
    SeatResponse response = seatService.updateSeatAvailability(id, available);
    return ResponseEntity.ok(response);
  }
}
