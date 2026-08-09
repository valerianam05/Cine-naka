package com.example.demo.mapper;

import com.example.demo.dto.request.CreateMovieRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.dto.response.MovieSummary;
import com.example.demo.entity.Movie;
import com.example.demo.entity.enums.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponse toResponse(Movie movie);

    MovieSummary toSummary(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "genres", source = "genres")
    @Mapping(target = "duration", expression = "java(mapDuration(request.durationMinutes()))")
    Movie toEntity(CreateMovieRequest request);


    default List<String> mapGenresToString(List<Genre> genres) {
        return genres.stream().map(Enum::name).collect(Collectors.toList());
    }

    default List<Genre> mapGenres(List<String> genres) {
        return genres.stream().map(Genre::valueOf).collect(Collectors.toList());
    }

    default Duration mapDuration(long minutes) {
        return Duration.ofMinutes(minutes);
    }

    default long mapDurationToMinutes(Duration duration) {
        return duration.toMinutes();
    }
}