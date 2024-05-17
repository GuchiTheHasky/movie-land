package com.movieland.facade.impl;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.entity.Review;
import com.movieland.facade.MovieFacade;
import com.movieland.mapper.MovieMapper;
import com.movieland.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieFacade implements MovieFacade {

    private final CurrencyConverterService currencyConverterService;
    private final GenreService genreService;
    private final CountryService countryService;
    private final ReviewService reviewService;
    private final MovieService movieService;

    private final ExecutorService executor;

    private final MovieMapper movieMapper;

    private final MovieCache cache;


    @Override
    public MovieFullInfoDto findFullMovieInfoById(int movieId, Currency currency) {
        MovieFullInfoDto cachedMovie = cache.getFromCache(movieId, currency);
        if (cachedMovie != null) {
            return cachedMovie;
        }

        Movie movie = movieService.findMovieById(movieId);

        if (currency != null) {
            double price = currencyConverterService.convertFromUah(movie.getPrice(), currency);
            movie.setPrice(price);
        }

        Future<List<Genre>> genresFuture = executor.submit(() -> genreService.findByMovieId(movieId));
        Future<List<Country>> countriesFuture = executor.submit(() -> countryService.findByMovieId(movieId));
        Future<List<Review>> reviewsFuture = executor.submit(() -> reviewService.findByMovieId(movieId));

        try {
            int timeout = 5;
            movie.setGenres(genresFuture.get(timeout, TimeUnit.SECONDS));
            movie.setCountries(countriesFuture.get(timeout, TimeUnit.SECONDS));
            movie.setReviews(reviewsFuture.get(timeout, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            log.error("Error fetching data: ", e);
        }

        MovieFullInfoDto movieDto = movieMapper.toMovieFullInfoDto(movie);

        return cache.saveToCache(movieId, movieDto);
    }

    @Override
    public MovieFullInfoDto update(int id, MovieAdminDto movieAdminDto) {
        return movieMapper.toMovieFullInfoDto(movieService.updateMovie(id, movieAdminDto));
    }

}
