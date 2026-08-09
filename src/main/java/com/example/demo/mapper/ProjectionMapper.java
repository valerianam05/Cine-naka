package com.example.demo.mapper;

import com.example.demo.dto.request.CreateProjectionRequest;
import com.example.demo.dto.response.ProjectionResponse;
import com.example.demo.entity.Projections;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {MovieMapper.class, RoomMapper.class})
public interface ProjectionMapper {

  @Mapping(target = "movie", source = "movie")
  @Mapping(target = "room", source = "room")
  ProjectionResponse toResponse(Projections projection);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "movie", ignore = true)
  @Mapping(target = "room", ignore = true)
  Projections toEntity(CreateProjectionRequest request);
}
