package com.example.demo.service;

import com.example.demo.dto.request.CreateProjectionRequest;
import com.example.demo.dto.response.ProjectionResponse;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Projections;
import com.example.demo.entity.Room;
import com.example.demo.mapper.ProjectionMapper;
import com.example.demo.repository.ProjectionRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectionService {

  private final ProjectionRepository projectionRepository;
  private final ProjectionMapper projectionMapper;
  private final MovieService movieService;
  private final RoomService roomService;

  @Transactional(readOnly = true)
  public List<ProjectionResponse> getAllProjections() {
    return projectionRepository.findAll().stream()
        .map(projectionMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public ProjectionResponse createOrUpdateProjection(CreateProjectionRequest request) {
    Movie movie = movieService.getMovieEntity(request.movieId());
    Room room = roomService.getRoomEntity(request.roomId());

    Projections projection = projectionMapper.toEntity(request);
    projection.setMovie(movie);
    projection.setRoom(room);

    if (projection.getId() == null) {
      projection.setId(UUID.randomUUID());
    }

    Projections saved = projectionRepository.save(projection);
    return projectionMapper.toResponse(saved);
  }

  @Transactional(readOnly = true)
  public ProjectionResponse getProjectionById(UUID id) {
    Projections projection =
        projectionRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Projection not found with id: " + id));
    return projectionMapper.toResponse(projection);
  }

  @Transactional(readOnly = true)
  public Projections getProjectionEntity(UUID id) {
    return projectionRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Projection not found with id: " + id));
  }
}
