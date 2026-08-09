package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.request.CreateProjectionRequest;
import com.example.demo.dto.response.MovieSummary;
import com.example.demo.dto.response.ProjectionResponse;
import com.example.demo.dto.response.RoomSummary;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Projections;
import com.example.demo.entity.Room;
import com.example.demo.mapper.ProjectionMapper;
import com.example.demo.repository.ProjectionRepository;
import java.math.BigDecimal;
import java.time.Instant;
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
class ProjectionServiceTest {

    @Mock private ProjectionRepository projectionRepository;
    @Mock private ProjectionMapper projectionMapper;
    @Mock private MovieService movieService;
    @Mock private RoomService roomService;

    @InjectMocks private ProjectionService projectionService;

    private Projections projection;
    private ProjectionResponse projectionResponse;
    private CreateProjectionRequest createRequest;
    private Movie movie;
    private Room room;

    @BeforeEach
    void setUp() {
        UUID projectionId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();

        movie = Movie.builder().id(movieId).title("Inception").build();
        room = Room.builder().id(roomId).number("A1").capacity(100).build();

        projection = Projections.builder()
                .id(projectionId)
                .dateTime(Instant.now())
                .seatPrice(BigDecimal.valueOf(12.50))
                .movie(movie)
                .room(room)
                .build();

        MovieSummary movieSummary = new MovieSummary(movieId, "Inception");
        RoomSummary roomSummary = new RoomSummary(roomId, "A1");
        projectionResponse = new ProjectionResponse(projectionId, projection.getDateTime(),
                projection.getSeatPrice(), movieSummary, roomSummary);

        createRequest = new CreateProjectionRequest(Instant.now(), BigDecimal.valueOf(12.50),
                movieId, roomId);
    }

    @Test
    void getAllProjections_shouldReturnList() {
        when(projectionRepository.findAll()).thenReturn(List.of(projection));
        when(projectionMapper.toResponse(projection)).thenReturn(projectionResponse);

        List<ProjectionResponse> result = projectionService.getAllProjections();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(projectionResponse);
    }

    @Test
    void createOrUpdateProjection_shouldCreateNewProjection() {
        when(movieService.getMovieEntity(createRequest.movieId())).thenReturn(movie);
        when(roomService.getRoomEntity(createRequest.roomId())).thenReturn(room);
        when(projectionMapper.toEntity(createRequest)).thenReturn(projection);
        when(projectionRepository.save(any(Projections.class))).thenReturn(projection);
        when(projectionMapper.toResponse(projection)).thenReturn(projectionResponse);

        ProjectionResponse result = projectionService.createOrUpdateProjection(createRequest);

        assertThat(result).isEqualTo(projectionResponse);
    }

    @Test
    void getProjectionById_shouldReturnProjection() {
        UUID id = projection.getId();
        when(projectionRepository.findById(id)).thenReturn(Optional.of(projection));
        when(projectionMapper.toResponse(projection)).thenReturn(projectionResponse);

        ProjectionResponse result = projectionService.getProjectionById(id);

        assertThat(result).isEqualTo(projectionResponse);
    }

    @Test
    void getProjectionById_shouldThrowRuntimeException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(projectionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectionService.getProjectionById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Projection not found");
    }

    @Test
    void getProjectionEntity_shouldReturnEntity() {
        UUID id = projection.getId();
        when(projectionRepository.findById(id)).thenReturn(Optional.of(projection));

        Projections result = projectionService.getProjectionEntity(id);

        assertThat(result).isEqualTo(projection);
    }
}