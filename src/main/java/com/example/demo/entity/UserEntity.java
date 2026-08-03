package com.example.demo.entity;

import com.example.demo.entity.enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

import com.example.demo.entity.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  private LocalDate birthdate;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  protected UserEntity() {}

}
