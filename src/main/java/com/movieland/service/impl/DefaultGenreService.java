package com.movieland.service.impl;

import com.movieland.dto.GenreDto;
import com.movieland.entity.Genre;
import com.movieland.repository.GenreRepository;
import com.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {

    private final GenresCache genresCache;

    private final GenreRepository genreRepository;


    @Override
    public List<GenreDto> findAll() {
        return genresCache.findAll();
    }

    @Override
    public List<Genre> findALlById(List<Integer> genreIds) {
        return genreRepository.findAllById(genreIds);
    }

    @Override
    public List<Genre> findByMovieId(int movieId) {
        return genreRepository.findByMovieId(movieId);
    }

}
