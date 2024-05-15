package com.movieland.web.controller;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieExtendedDto;
import com.movieland.mapper.MovieMapper;
import com.movieland.web.controller.validation.SortOrderPrice;
import com.movieland.web.controller.validation.SortOrderRating;
import com.movieland.dto.MovieDto;
import com.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public MovieExtendedDto findMoviesById(
            @PathVariable int movieId,
            @RequestParam(required = false) Currency currency) {
        return movieMapper.toMovieExtendedDto(movieService.findMovieById(movieId, currency));
    }

    @PostMapping
    public void saveMovie(@RequestBody MovieAdminDto movieAdminDto) {
        log.info("Saving movie");
        movieService.saveMovie(movieAdminDto);
    }

    @PutMapping("/{id}")
    public void editMovie(@PathVariable int id, @RequestBody MovieAdminDto movieAdminDto) {
        log.info("Editing movie");
        movieService.editMovie(movieAdminDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable int id) {
        log.info("Deleting movie");
        movieService.deleteMovie(id);
    }
}
