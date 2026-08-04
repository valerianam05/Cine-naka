package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(name = "duration_minutes")
  private Duration durationMinutes;

  @ElementCollection(targetClass = main.java.com.example.demo.entity.enums.Genre.class)
  @Enumerated(EnumType.STRING)
  private List<main.java.com.example.demo.entity.enums.Genre> genres;
}
