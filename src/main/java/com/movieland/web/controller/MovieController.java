package com.movieland.web.controller;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.mapper.MovieMapper;
import com.movieland.service.MovieCacheService;
import com.movieland.web.controller.validation.SortOrderPrice;
import com.movieland.web.controller.validation.SortOrderRating;
import com.movieland.dto.MovieDto;
import com.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return movieService.findMovieById(movieId, currency);
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
}
