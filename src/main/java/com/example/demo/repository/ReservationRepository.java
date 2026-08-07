package com.example.demo.repository;

import com.example.demo.entity.ReservationEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {}
