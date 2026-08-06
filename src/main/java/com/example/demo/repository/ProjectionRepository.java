package com.example.demo.repository;

import com.example.demo.entity.Projections;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends JpaRepository<Projections, UUID> {

  List<Projections> findByMovieId(UUID movieId);

  List<Projections> findByDateTimeAfter(Instant dateTime);
}