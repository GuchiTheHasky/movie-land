package com.movieland.service.impl;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.entity.Country;
import com.movieland.entity.EnrichmentType;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.exception.MovieNotFoundException;
import com.movieland.mapper.MovieMapper;
import com.movieland.repository.MovieRepository;
import com.movieland.service.*;
import com.movieland.web.controller.validation.SortOrderPrice;
import com.movieland.web.controller.validation.SortOrderRating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {

    private final GenreService genreService;
    private final CountryService countryService;
    private final MovieCacheService movieCacheService;
    private final CurrencyConverterService currencyConverterService;
    private final EnrichmentService enrichmentService;

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;


    @Override
    public List<Movie> findAllMovies(SortOrderRating rating, SortOrderPrice price) {
        Pair<String, String> validateQuery = validateQuery(rating, price);
        String sortBy = validateQuery.getLeft();
        String sortOrder = validateQuery.getRight();

        return movieRepository.findAllCustomSortedMovies(sortBy, sortOrder);
    }

    @Override
    public List<Movie> findMoviesByGenre(int genreId, SortOrderRating rating, SortOrderPrice price) {
        Pair<String, String> validateQuery = validateQuery(rating, price);
        String sortBy = validateQuery.getLeft();
        String sortOrder = validateQuery.getRight();

        return movieRepository.findAllByGenreIdCustomSortedMovies(genreId, sortBy, sortOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieFullInfoDto findMovieById(int movieId, Currency currency) {
        MovieFullInfoDto cachedMovie = movieCacheService.findInCache(movieId, currency);
        if (cachedMovie != null) {
            return cachedMovie;
        }

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException(movieId));

        assignCurrencyConversion(currency, movie);
        enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.values());

        MovieFullInfoDto movieDto = movieMapper.toMovieFullInfoDto(movie);

        return movieCacheService.cacheAndReturn(movieId, movieDto);
    }

    private void assignCurrencyConversion(Currency currency, Movie movie) {
        if (currency != null) {
            double price = currencyConverterService.convertFromUah(movie.getPrice(), currency);
            movie.setPrice(price);
        }
    }


    @Override
    public void saveMovie(MovieAdminDto movieAdminDto) {

        Movie movie = Movie.builder()
                .nameRussian(movieAdminDto.getNameRussian())
                .nameNative(movieAdminDto.getNameNative())
                .picturePath(movieAdminDto.getPicturePath())
                .yearOfRelease(movieAdminDto.getYearOfRelease())
                .price(movieAdminDto.getPrice())
                .rating(movieAdminDto.getRating())
                .description(movieAdminDto.getDescription())
                .genres(genreService.findALlById(movieAdminDto.getGenres()))
                .countries(countryService.findAllCountriesById(movieAdminDto.getCountries()))
                .version(0)
                .build();

        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public MovieFullInfoDto update(int id, MovieAdminDto movieAdminDto) {
        Movie movie = findMovieByReferenceId(id);

        List<Genre> genres = genreService.findALlById(movieAdminDto.getGenres());
        List<Country> countries = countryService.findAllCountriesById(movieAdminDto.getCountries());

        Movie updatedMovie = movieMapper.update(movie, movieAdminDto, countries, genres);
        MovieFullInfoDto movieFullInfoDto = movieMapper.toMovieFullInfoDto(updatedMovie);

        movieRepository.save(updatedMovie);
        return movieCacheService.cacheAndReturn(id, movieFullInfoDto);
    }

    @Override
    public MovieFullInfoDto mapToMovieFullInfoDto(Movie movie) {
        return movieMapper.toMovieFullInfoDto(movie);
    }

    @Override
    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    @Override
    public Movie findMovieByReferenceId(int movieId) {
        return movieRepository.getReferenceById(movieId);
    }

    @Override
    public List<Movie> findRandomMovies() {
        return movieRepository.findThreeRandomMovies();
    }



    private Pair<String, String> validateQuery(SortOrderRating rating, SortOrderPrice price) {

        String sortBy = "id";
        String sortOrder = "asc";
        if (price == null && rating != null && rating.toString().equalsIgnoreCase("desc")) {
            sortBy = "rating";
            sortOrder = "desc";
        }
        if (rating == null && price != null && price.toString().equalsIgnoreCase("asc")) {
            sortBy = "price";
        }
        if (rating == null && price != null && price.toString().equalsIgnoreCase("desc")) {
            sortBy = "price";
            sortOrder = "desc";
        }
        return Pair.of(sortBy, sortOrder);
    }

}

