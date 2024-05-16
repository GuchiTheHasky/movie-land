package com.movieland.repository;

import com.movieland.entity.Country;
import com.movieland.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepositoryCustom {
    List<Movie> findAllCustomSortedMovies(String sortBy, String sortOrder);

    List<Movie> findAllByGenreIdCustomSortedMovies(int genreId, String sortBy, String sortOrder);

    List<Country> findCountryByMovieId(int movieId);
}
