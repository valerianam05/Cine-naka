package com.example.demo.repository;

import com.example.demo.entity.Projection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, UUID> {

  List<Projection> findByMovieId(UUID movieId);

  List<Projection> findByDateTimeAfter(Instant dateTime);
}
