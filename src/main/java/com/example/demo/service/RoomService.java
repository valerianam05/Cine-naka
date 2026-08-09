package com.example.demo.service;

import com.example.demo.dto.request.CreateRoomRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.entity.Room;
import com.example.demo.entity.Seat;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.SeatRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final SeatRepository seatRepository;
  private final RoomMapper roomMapper;

  @Transactional
  public RoomResponse createRoom(CreateRoomRequest request) {
    Room room = roomMapper.toEntity(request);
    Room savedRoom = roomRepository.save(room);

    if (request.seats() != null && !request.seats().isEmpty()) {
      List<Seat> seats =
          request.seats().stream()
              .map(
                  seatReq ->
                      Seat.builder()
                          .number(seatReq.number())
                          .isAvailable(seatReq.available())
                          .room(savedRoom)
                          .build())
              .collect(Collectors.toList());
      seatRepository.saveAll(seats);
      savedRoom.setSeats(seats);
    }
    return roomMapper.toResponse(savedRoom);
  }

  @Transactional(readOnly = true)
  public List<RoomResponse> getAllRooms() {
    return roomRepository.findAll().stream()
        .map(roomMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public RoomResponse getRoomById(UUID id) {
    Room room =
        roomRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    return roomMapper.toResponse(room);
  }

  @Transactional(readOnly = true)
  public Room getRoomEntity(UUID id) {
    return roomRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
  }

  @Transactional
  public RoomResponse updateRoom(UUID id, CreateRoomRequest request) {
    Room room =
        roomRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    room.setNumber(request.number());
    room.setCapacity(request.capacity());
    return roomMapper.toResponse(roomRepository.save(room));
  }

  @Transactional
  public void deleteRoom(UUID id) {
    if (!roomRepository.existsById(id)) {
      throw new RuntimeException("Room not found with id: " + id);
    }
    roomRepository.deleteById(id);
  }
}
