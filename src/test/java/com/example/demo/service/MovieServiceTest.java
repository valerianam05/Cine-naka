package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.request.CreateMovieRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.entity.Movie;
import com.example.demo.entity.enums.Genre;
import com.example.demo.mapper.MovieMapper;
import com.example.demo.repository.MovieRepository;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock private MovieRepository movieRepository;
    @Mock private MovieMapper movieMapper;

    @InjectMocks private MovieService movieService;

    private Movie movie;
    private MovieResponse movieResponse;
    private CreateMovieRequest createRequest;

    @BeforeEach
    void setUp() {
        UUID movieId = UUID.randomUUID();
        movie = Movie.builder()
                .id(movieId)
                .title("Inception")
                .genres(List.of(Genre.SCIFI, Genre.ACTION))
                .description("A mind-bending thriller")
                .duration(Duration.ofMinutes(148))
                .build();

        movieResponse = new MovieResponse(movieId, "Inception", List.of("SCIFI", "ACTION"),
                "A mind-bending thriller", 148L);

        createRequest = new CreateMovieRequest("Inception", List.of("SCIFI", "ACTION"),
                "A mind-bending thriller", 148L);
    }

    @Test
    void createOrUpdateMovie_shouldCreateNewMovie() {
        when(movieMapper.toEntity(createRequest)).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(movieMapper.toResponse(movie)).thenReturn(movieResponse);

        MovieResponse result = movieService.createOrUpdateMovie(createRequest);

        assertThat(result).isEqualTo(movieResponse);
    }

    @Test
    void getMovieById_shouldReturnMovie() {
        UUID id = movie.getId();
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(movieMapper.toResponse(movie)).thenReturn(movieResponse);

        MovieResponse result = movieService.getMovieById(id);

        assertThat(result).isEqualTo(movieResponse);
    }

    @Test
    void getMovieById_shouldThrowRuntimeException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getMovieById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Movie not found");
    }

    @Test
    void getMovieEntity_shouldReturnEntity() {
        UUID id = movie.getId();
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieEntity(id);

        assertThat(result).isEqualTo(movie);
    }
}
