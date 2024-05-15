package com.movieland.service.impl;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.entity.Movie;
import com.movieland.repository.MovieRepository;
import com.movieland.service.CountryService;
import com.movieland.service.CurrencyConverterService;
import com.movieland.service.GenreService;
import com.movieland.service.MovieService;
import com.movieland.web.controller.validation.SortOrderPrice;
import com.movieland.web.controller.validation.SortOrderRating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {

    private final MovieRepository movieRepository;
    private final CurrencyConverterService currencyConverterService;
    private final GenreService genreService;
    private final CountryService countryService;


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
    public Movie findMovieById(int movieId, Currency currency) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            if (currency != null) {
                double price = currencyConverterService.convertFromUah(movie.getPrice(), currency);
                movie.setPrice(price);
                return movie;
            }
            return movie;
        }
        return null;
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
    public void saveMovie(Movie movie) {

        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void editMovie(MovieAdminDto movieAdminDto, int id) {

        Movie movie = findMovieByReferenceId(id);

        if (movieAdminDto.getNameRussian() != null) {
            movie.setNameRussian(movieAdminDto.getNameRussian());
        }
        if (movieAdminDto.getNameNative() != null) {
            movie.setNameNative(movieAdminDto.getNameNative());
        }
        if (movieAdminDto.getPicturePath() != null) {
            movie.setPicturePath(movieAdminDto.getPicturePath());
        }
        if (movieAdminDto.getYearOfRelease() != 0) {
            movie.setYearOfRelease(movieAdminDto.getYearOfRelease());
        }
        if (movieAdminDto.getRating() != null) {
            movie.setRating(movieAdminDto.getRating());
        }
        if (movieAdminDto.getPrice() != null) {
            movie.setPrice(movieAdminDto.getPrice());
        }
        if (movieAdminDto.getDescription() != null) {
            movie.setDescription(movieAdminDto.getDescription());
        }
        if (movieAdminDto.getGenres() != null) {
            movie.setGenres(genreService.findALlById(movieAdminDto.getGenres()));
        }
        if (movieAdminDto.getCountries() != null) {
            movie.setCountries(countryService.findAllCountriesById(movieAdminDto.getCountries()));
        }

        movieRepository.save(movie);

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



