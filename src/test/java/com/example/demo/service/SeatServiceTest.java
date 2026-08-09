package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.demo.dto.response.SeatResponse;
import com.example.demo.entity.Seat;
import com.example.demo.mapper.RoomMapper;
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
class SeatServiceTest {

    @Mock private SeatRepository seatRepository;
    @Mock private RoomMapper roomMapper;

    @InjectMocks private SeatService seatService;

    private Seat seat;
    private SeatResponse seatResponse;

    @BeforeEach
    void setUp() {
        UUID seatId = UUID.randomUUID();
        seat = Seat.builder()
                .id(seatId)
                .number("A1")
                .isAvailable(true)
                .build();

        seatResponse = new SeatResponse(seatId, "A1", true);
    }

    @Test
    void getSeatsByRoom_shouldReturnList() {
        UUID roomId = UUID.randomUUID();
        when(seatRepository.findByRoomId(roomId)).thenReturn(List.of(seat));
        when(roomMapper.toSeatResponse(seat)).thenReturn(seatResponse);

        List<SeatResponse> result = seatService.getSeatsByRoom(roomId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(seatResponse);
    }

    @Test
    void getSeatEntity_shouldReturnSeat() {
        UUID id = seat.getId();
        when(seatRepository.findById(id)).thenReturn(Optional.of(seat));

        Seat result = seatService.getSeatEntity(id);

        assertThat(result).isEqualTo(seat);
    }

    @Test
    void getSeatEntity_shouldThrowRuntimeException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(seatRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seatService.getSeatEntity(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Seat not found");
    }

    @Test
    void updateSeatAvailability_shouldUpdateAndReturnSeat() {
        UUID id = seat.getId();
        boolean newAvailability = false;
        Seat updatedSeat = Seat.builder()
                .id(id)
                .number("A1")
                .isAvailable(newAvailability)
                .build();

        when(seatRepository.findById(id)).thenReturn(Optional.of(seat));
        when(seatRepository.save(seat)).thenReturn(updatedSeat);
        when(roomMapper.toSeatResponse(updatedSeat)).thenReturn(
                new SeatResponse(id, "A1", newAvailability));

        SeatResponse result = seatService.updateSeatAvailability(id, newAvailability);

        assertThat(result.available()).isFalse();
    }
}
