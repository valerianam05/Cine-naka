package com.example.demo.mapper;

import com.example.demo.dto.request.CreateRoomRequest;
import com.example.demo.dto.request.CreateSeatRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.dto.response.RoomSummary;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.entity.Room;
import com.example.demo.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomResponse toResponse(Room room);

    RoomSummary toSummary(Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seats", ignore = true)
    Room toEntity(CreateRoomRequest request);

    SeatResponse toSeatResponse(Seat seat);

    default Seat toSeatEntity(CreateSeatRequest request, Room room) {
        return Seat.builder()
                .number(request.number())
                .isAvailable(request.available())
                .room(room)
                .build();
    }
}