package com.example.demo.mapper;

import com.example.demo.dto.request.CreateMovieRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.dto.response.MovieSummary;
import com.example.demo.entity.Movie;
import com.example.demo.entity.enums.Genre;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MovieMapper {

  @Mapping(source = "duration", target = "durationMinutes", qualifiedByName = "durationToMinutes")
  MovieResponse toResponse(Movie movie);

  MovieSummary toSummary(Movie movie);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "durationMinutes", target = "duration", qualifiedByName = "minutesToDuration")
  @Mapping(source = "genres", target = "genres", qualifiedByName = "stringToGenreList")
  Movie toEntity(CreateMovieRequest request);

  @Named("durationToMinutes")
  default long durationToMinutes(Duration duration) {
    return duration.toMinutes();
  }

  @Named("minutesToDuration")
  default Duration minutesToDuration(long minutes) {
    return Duration.ofMinutes(minutes);
  }

  @Named("stringToGenreList")
  default List<Genre> stringToGenreList(List<String> genres) {
    return genres.stream().map(Genre::valueOf).collect(Collectors.toList());
  }
}
