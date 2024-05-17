package com.movieland.facade.impl;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.entity.Review;
import com.movieland.facade.MovieFacade;
import com.movieland.service.CurrencyConverterService;
import com.movieland.service.CountryService;
import com.movieland.service.ReviewService;
import com.movieland.service.MovieService;
import com.movieland.service.GenreService;
import com.movieland.service.MovieCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieFacade implements MovieFacade {

    private final CurrencyConverterService currencyConverterService;
    private final CountryService countryService;
    private final ReviewService reviewService;
    private final MovieService movieService;
    private final GenreService genreService;
    private final MovieCacheService cache;

    private final ExecutorService executor;


    @Override
    @Transactional(readOnly = true)
    public MovieFullInfoDto findFullMovieInfoById(int movieId, Currency currency) {
        MovieFullInfoDto cachedMovie = cache.getFromCache(movieId, currency);
        if (cachedMovie != null) {
            return cachedMovie;
        }

        Movie movie = movieService.findMovieById(movieId);
        handleCurrencyConversion(currency, movie);

        Future<List<Genre>> genresFuture = executor.submit(() -> genreService.findByMovieId(movieId));
        Future<List<Country>> countriesFuture = executor.submit(() -> countryService.findByMovieId(movieId));
        Future<List<Review>> reviewsFuture = executor.submit(() -> reviewService.findByMovieId(movieId));

        enrichMovieData(movie, genresFuture, countriesFuture, reviewsFuture);
        MovieFullInfoDto movieDto = movieService.mapToMovieFullInfoDto(movie);

        return cache.handleCacheIfAbsent(movieId, movieDto);
    }

    @Override
    @Transactional
    public MovieFullInfoDto update(int id, MovieAdminDto movieAdminDto) {
        Movie updatedMovie = movieService.updateMovie(id, movieAdminDto);
        MovieFullInfoDto movieDto = movieService.mapToMovieFullInfoDto(updatedMovie);
        return cache.handleCacheIfAbsent(id, movieDto);
    }

    private void handleCurrencyConversion(Currency currency, Movie movie) {
        if (currency != null) {
            double price = currencyConverterService.convertFromUah(movie.getPrice(), currency);
            movie.setPrice(price);
        }
    }

    private void enrichMovieData(Movie movie, Future<List<Genre>> genresFuture, Future<List<Country>> countriesFuture, Future<List<Review>> reviewsFuture) {
        int timeout = 5;
        try {
            movie.setGenres(genresFuture.get(timeout, TimeUnit.SECONDS));
            movie.setCountries(countriesFuture.get(timeout, TimeUnit.SECONDS));
            movie.setReviews(reviewsFuture.get(timeout, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            log.error("Error fetching data: ", e);
        }
    }
}
