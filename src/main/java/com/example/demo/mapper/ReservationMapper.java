package com.example.demo.mapper;

import com.example.demo.entity.ReservationEntity;
import com.example.demo.model.Reservation;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

  public Reservation toModel(ReservationEntity entity) {
    return new Reservation(
        entity.getId(),
        entity.getProjection().getId(),
        entity.getUser().getId(),
        entity.getStatus());
  }

  public List<Reservation> toModelList(List<ReservationEntity> entities) {
    return entities.stream().map(this::toModel).collect(Collectors.toList());
  }
}
