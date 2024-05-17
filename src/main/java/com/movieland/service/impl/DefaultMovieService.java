package com.movieland.service.impl;

import com.movieland.dto.MovieAdminDto;
import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.exception.MovieNotFoundException;
import com.movieland.mapper.MovieMapper;
import com.movieland.repository.MovieRepository;
import com.movieland.service.CountryService;
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

    private final GenreService genreService;
    private final CountryService countryService;

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
    public Movie findMovieById(int movieId) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isEmpty()) {
            throw new MovieNotFoundException(movieId);
        }
        return movieOptional.get();
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
    public Movie updateMovie(int id, MovieAdminDto movieAdminDto) {
        Movie movie = findMovieByReferenceId(id);

        List<Genre> genres = genreService.findALlById(movieAdminDto.getGenres());
        List<Country> countries = countryService.findAllCountriesById(movieAdminDto.getCountries());

        return movieRepository.save(movieMapper.update(movie, movieAdminDto, countries, genres));
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



