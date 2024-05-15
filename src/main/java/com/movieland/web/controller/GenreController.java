package com.movieland.web.controller;

import com.movieland.dto.GenreDto;
import com.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/genre")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> findAllGenres() {
        log.info("Getting all genres");
        return genreService.findAll();
    }
}









