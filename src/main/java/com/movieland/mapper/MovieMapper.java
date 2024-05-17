package com.movieland.mapper;

import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import org.mapstruct.Mapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface MovieMapper {

    List<MovieDto> toDto(List<Movie> movies);

    MovieFullInfoDto toMovieFullInfoDto(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "genres", ignore = true)
    Movie update(@MappingTarget Movie movie, MovieAdminDto movieAdminDto, @Context List<Country> countries, @Context List<Genre> genres);

    @AfterMapping
    default void afterUpdateMapping(@MappingTarget Movie movie, @Context List<Country> countries, @Context List<Genre> genres) {
        movie.setCountries(countries);
        movie.setGenres(genres);
    }
}
