package com.movieland.service;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.web.controller.validation.SortOrderPrice;
import com.movieland.web.controller.validation.SortOrderRating;
import com.movieland.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> findRandomMovies();

    List<Movie> findMoviesByGenre(int genreId, SortOrderRating rating, SortOrderPrice price);

    List<Movie> findAllMovies(SortOrderRating rating, SortOrderPrice price);

    MovieFullInfoDto findFullMovieInfoById(int movieId, Currency currencyValidation);

    void saveMovie(MovieAdminDto movieAdminDto);

    void saveMovie(Movie movie);

    void editMovie(MovieAdminDto movieAdminDto, int id);

    void deleteMovie(int id);

    Movie findMovieByReferenceId(int movieId);
}
