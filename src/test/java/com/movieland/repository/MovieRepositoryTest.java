package com.movieland.repository;

import com.movieland.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void cleanUp() {
        movieRepository.deleteAll();
    }

    @Test
    @DisplayName("Test - find movie by id")
    void givenMovie_whenFindById_thenReturnMovie() {
        // Given
        Movie movie = createMovie();
        movieRepository.save(movie);
        // When
        Optional<Movie> result = movieRepository.findById(movie.getId());
        // Then
        assertTrue(result.isPresent());
        assertEquals(movie.getId(), result.get().getId());
        assertEquals(movie.getNameNative(), result.get().getNameNative());
        assertEquals(movie.getNameRussian(), result.get().getNameRussian());
        assertEquals(movie.getYearOfRelease(), result.get().getYearOfRelease());
        assertEquals(movie.getDescription(), result.get().getDescription());
        assertEquals(movie.getRating(), result.get().getRating());
        assertEquals(movie.getPrice(), result.get().getPrice());
        assertEquals(movie.getPicturePath(), result.get().getPicturePath());
    }

    @Test
    @DisplayName("Test - find not existing movie by id, then return empty optional")
    void givenNotExistingMovie_whenFindById_thenReturnOptionalEmpty() {
        // Given
        Movie movie = createMovie();
        movieRepository.save(movie);
        int id = 2;
        // When
        Optional<Movie> result = movieRepository.findById(id);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test - find movie by id with null id")
    void givenMovie_whenFindByIdWithNullId_thenReturnInvalidDataAccessApiUsageException() {
        // Given
        Movie movie = createMovie();
        movieRepository.save(movie);
        String expectedMessage = "The given id must not be null";
        // Then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> movieRepository.findById(null), expectedMessage);
    }

    private Movie createMovie() {
        return Movie.builder()
                .nameRussian("Пятьдесят оттенков черного")
                .nameNative("Fifty Shades of Black")
                .yearOfRelease(2016)
                .description("""
                         Феномен успеха эротической мелодрамы «Пятьдесят оттенков серого» многим не дает покоя:
                         одни удивляются, другие возмущаются, а третьи открыто или тайно ждут не дождутся продолжения
                         нашумевшей истории...
                        """)
                .rating(1.0)
                .price(100.0)
                .picturePath("https://uafilm.pro/3820-pyatdesyat-vdtnkv-chornogo.html")
                .build();
    }


}