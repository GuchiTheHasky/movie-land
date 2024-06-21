package com.movieland.web.controller;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.exception.MovieIncomeDataException;
import com.movieland.exception.MovieNotFoundException;
import com.movieland.mapper.MovieMapper;
import com.movieland.web.controller.validation.SortOrderPrice;
import com.movieland.web.controller.validation.SortOrderRating;
import com.movieland.dto.MovieDto;
import com.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping
    public List<MovieDto> findAllMovies(
            @RequestParam(required = false) SortOrderRating rating,
            @RequestParam(required = false) SortOrderPrice price) {
        return movieMapper.toDto(movieService.findAllMovies(rating, price));
    }

    @GetMapping("/genre/{genreId}")
    public List<MovieDto> findMoviesByGenre(
            @PathVariable int genreId,
            @RequestParam(required = false) SortOrderRating rating,
            @RequestParam(required = false) SortOrderPrice price) {
        return movieMapper.toDto(movieService.findMoviesByGenre(genreId, rating, price));
    }

    @GetMapping("/random")
    public List<MovieDto> findRandomMovies() {
        return movieMapper.toDto(movieService.findRandomMovies());
    }

    @GetMapping("/{movieId}")
    public MovieFullInfoDto findMovieById(
            @PathVariable int movieId,
            @RequestParam(required = false) Currency currency) {
        MovieFullInfoDto movie = movieService.findMovieById(movieId, currency);
        return movie;
    }

    @PutMapping("/{id}")
    public MovieFullInfoDto updateMovie(@PathVariable int id, @RequestBody MovieAdminDto movieAdminDto) {
        return movieService.update(id, movieAdminDto);
    }

    @PostMapping
    public void saveMovie(@RequestBody MovieAdminDto movieAdminDto) {
        movieService.saveMovie(movieAdminDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable int id) {
        movieService.deleteMovie(id);
    }

    @ExceptionHandler(MovieIncomeDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMovieIncomeDataException(MovieIncomeDataException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleMovieNotFoundExceptionException(MovieNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }
}
