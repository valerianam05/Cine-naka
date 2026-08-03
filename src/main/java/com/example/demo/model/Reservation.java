package com.example.demo.model;

import com.example.demo.entity.enums.ReservationStatus;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
  private UUID id;
  private UUID projectionId;
  private UUID userId;
  private ReservationStatus status;
}
