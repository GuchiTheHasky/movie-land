package com.movieland.mapper;

import com.movieland.dto.MovieDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.entity.Movie;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)

public interface MovieMapper {

    List<MovieDto> toDto(List<Movie> movies);

    MovieFullInfoDto toMovieExtendedDto(Movie movie);
}
