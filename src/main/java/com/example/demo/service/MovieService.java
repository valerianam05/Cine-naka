package com.example.demo.service;

import com.example.demo.dto.request.CreateMovieRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.entity.Movie;
import com.example.demo.mapper.MovieMapper;
import com.example.demo.repository.MovieRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  @Transactional
  public MovieResponse createOrUpdateMovie(CreateMovieRequest request) {
    Movie movie = movieMapper.toEntity(request);
    if (movie.getId() == null) {
      movie.setId(UUID.randomUUID());
    }
    Movie saved = movieRepository.save(movie);
    return movieMapper.toResponse(saved);
  }

  @Transactional(readOnly = true)
  public MovieResponse getMovieById(UUID id) {
    Movie movie =
        movieRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    return movieMapper.toResponse(movie);
  }

  @Transactional(readOnly = true)
  public Movie getMovieEntity(UUID id) {
    return movieRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
  }
}
