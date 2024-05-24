package com.movieland.service.impl;

import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.entity.Review;
import com.movieland.repository.CountryRepository;
import com.movieland.repository.GenreRepository;
import com.movieland.repository.ReviewRepository;
import com.movieland.service.EnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEnrichmentService implements EnrichmentService {

    private static final Integer TIME_OUT = 5;

    private final ExecutorService executor;

    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public void enrichAdditionalInfo(int movieId, Movie movie) {
        try {
            Callable<List<Genre>> genreTask = () -> genreRepository.findByMovieId(movieId);
            Callable<List<Country>> countriesTask = () -> countryRepository.findByMovieId(movieId);
            Callable<List<Review>> reviewsTask = () -> reviewRepository.findByMovieId(movieId);

            List<Genre> genresFuture = executor.invokeAny(Collections.singleton(genreTask), TIME_OUT, TimeUnit.SECONDS);
            List<Country> countriesFuture = executor.invokeAny(Collections.singleton(countriesTask), TIME_OUT, TimeUnit.SECONDS);
            List<Review> reviewsFuture = executor.invokeAny(Collections.singleton(reviewsTask), TIME_OUT, TimeUnit.SECONDS);

            enrichInfo(movie, genresFuture, countriesFuture, reviewsFuture);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Error fetching data: ", e);
            Thread.currentThread().interrupt();
        }
    }

    private void enrichInfo(Movie movie, List<Genre> genresFuture, List<Country> countriesFuture, List<Review> reviewsFuture) {
        movie.setGenres(genresFuture);
        movie.setCountries(countriesFuture);
        movie.setReviews(reviewsFuture);
    }

}
