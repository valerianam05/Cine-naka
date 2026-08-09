package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.request.CreateRoomRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.service.RoomService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @PostMapping
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
    RoomResponse response = roomService.createRoom(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  public ResponseEntity<List<RoomResponse>> getAllRooms() {
    return ResponseEntity.ok(roomService.getAllRooms());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  public ResponseEntity<RoomResponse> getRoomById(@PathVariable UUID id) {
    return ResponseEntity.ok(roomService.getRoomById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<RoomResponse> updateRoom(
      @PathVariable UUID id, @Valid @RequestBody CreateRoomRequest request) {
    RoomResponse response = roomService.updateRoom(id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<Void> deleteRoom(@PathVariable UUID id) {
    roomService.deleteRoom(id);
    return ResponseEntity.noContent().build();
  }
}
