package com.example.demo.entity;

import com.example.demo.entity.enums.Genre;
import jakarta.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @ElementCollection(targetClass = Genre.class)
  @Enumerated(EnumType.STRING)
  private List<Genre> genres;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Duration duration;
}
