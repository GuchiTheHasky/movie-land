package com.movieland.service;

import com.movieland.dto.MovieAdminDto;
import com.movieland.web.controller.validation.SortOrderPrice;
import com.movieland.web.controller.validation.SortOrderRating;
import com.movieland.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> findRandomMovies();

    List<Movie> findMoviesByGenre(int genreId, SortOrderRating rating, SortOrderPrice price);

    List<Movie> findAllMovies(SortOrderRating rating, SortOrderPrice price);

    Movie findMovieById(int movieId);

    //MovieFullInfoDto findFullMovieInfoById(int movieId, Currency currencyValidation);

    void saveMovie(MovieAdminDto movieAdminDto);

    void saveMovie(Movie movie);

    Movie updateMovie(int id, MovieAdminDto movieAdminDto);

    void deleteMovie(int id);

    Movie findMovieByReferenceId(int movieId);
}
