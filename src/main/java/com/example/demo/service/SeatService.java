package com.example.demo.service;

import com.example.demo.dto.response.SeatResponse;
import com.example.demo.entity.Seat;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.repository.SeatRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {

  private final SeatRepository seatRepository;
  private final RoomMapper roomMapper;

  @Transactional(readOnly = true)
  public List<SeatResponse> getSeatsByRoom(UUID roomId) {
    return seatRepository.findByRoomId(roomId).stream()
        .map(roomMapper::toSeatResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Seat getSeatEntity(UUID seatId) {
    return seatRepository
        .findById(seatId)
        .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
  }

  @Transactional
  public SeatResponse updateSeatAvailability(UUID seatId, boolean available) {
    Seat seat = getSeatEntity(seatId);
    seat.setIsAvailable(available);
    Seat saved = seatRepository.save(seat);
    return roomMapper.toSeatResponse(saved);
  }
}
