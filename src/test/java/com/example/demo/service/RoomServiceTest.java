package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.request.CreateRoomRequest;
import com.example.demo.dto.request.CreateSeatRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.entity.Room;
import com.example.demo.entity.Seat;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.SeatRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock private RoomRepository roomRepository;
    @Mock private SeatRepository seatRepository;
    @Mock private RoomMapper roomMapper;

    @InjectMocks private RoomService roomService;

    private Room room;
    private RoomResponse roomResponse;
    private CreateRoomRequest createRequest;

    @BeforeEach
    void setUp() {
        UUID roomId = UUID.randomUUID();
        UUID seat1Id = UUID.randomUUID();
        UUID seat2Id = UUID.randomUUID();

        room = Room.builder()
                .id(roomId)
                .number("A1")
                .capacity(100)
                .seats(List.of(
                        Seat.builder().id(seat1Id).number("A1").isAvailable(true).build(),
                        Seat.builder().id(seat2Id).number("A2").isAvailable(true).build()
                ))
                .build();

        roomResponse = new RoomResponse(roomId, "A1", 100, List.of(
                new SeatResponse(seat1Id, "A1", true),
                new SeatResponse(seat2Id, "A2", true)
        ));

        createRequest = new CreateRoomRequest("A1", 100, List.of(
                new CreateSeatRequest("A1", true),
                new CreateSeatRequest("A2", true)
        ));
    }

    @Test
    void createRoom_shouldCreateRoomWithSeats() {
        when(roomMapper.toEntity(createRequest)).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toResponse(room)).thenReturn(roomResponse);

        RoomResponse result = roomService.createRoom(createRequest);

        assertThat(result).isEqualTo(roomResponse);
        verify(seatRepository).saveAll(any());
    }

    @Test
    void getAllRooms_shouldReturnList() {
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(roomMapper.toResponse(room)).thenReturn(roomResponse);

        List<RoomResponse> result = roomService.getAllRooms();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(roomResponse);
    }

    @Test
    void getRoomById_shouldReturnRoom() {
        UUID id = room.getId();
        when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        when(roomMapper.toResponse(room)).thenReturn(roomResponse);

        RoomResponse result = roomService.getRoomById(id);

        assertThat(result).isEqualTo(roomResponse);
    }

    @Test
    void getRoomById_shouldThrowRuntimeException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.getRoomById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Room not found");
    }

    @Test
    void getRoomEntity_shouldReturnEntity() {
        UUID id = room.getId();
        when(roomRepository.findById(id)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomEntity(id);

        assertThat(result).isEqualTo(room);
    }

    @Test
    void updateRoom_shouldUpdateRoom() {
        UUID id = room.getId();
        when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toResponse(room)).thenReturn(roomResponse);

        RoomResponse result = roomService.updateRoom(id, createRequest);

        assertThat(result).isEqualTo(roomResponse);
        assertThat(room.getNumber()).isEqualTo(createRequest.number());
        assertThat(room.getCapacity()).isEqualTo(createRequest.capacity());
    }

    @Test
    void deleteRoom_shouldDeleteRoom() {
        UUID id = room.getId();
        when(roomRepository.existsById(id)).thenReturn(true);

        roomService.deleteRoom(id);

        verify(roomRepository).deleteById(id);
    }

    @Test
    void deleteRoom_shouldThrowRuntimeException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(roomRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> roomService.deleteRoom(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Room not found");
    }
}