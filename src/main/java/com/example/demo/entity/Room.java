package com.example.demo.entity;

import jakarta.persistence.*;
<<<<<<< HEAD:src/main/java/com/example/demo/entity/Room.java
import java.util.List;
=======
import java.time.Duration;
>>>>>>> ec712e6 (chore(test): add temporary security config and reservation API testing setup):src/main/java/com/example/demo/entity/MovieEntity.java
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String number;

<<<<<<< HEAD:src/main/java/com/example/demo/entity/Room.java
  @Column(nullable = false)
  private Integer capacity;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Seat> seats;
=======
  @Column(name = "duration_minutes")
  private Duration durationMinutes;
>>>>>>> ec712e6 (chore(test): add temporary security config and reservation API testing setup):src/main/java/com/example/demo/entity/MovieEntity.java
}
