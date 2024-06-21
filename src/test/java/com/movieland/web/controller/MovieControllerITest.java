package com.movieland.web.controller;

import com.movieland.config.H2ServerConfiguration;
import com.movieland.config.RedisConfiguration;
import com.movieland.config.TestSecurityConfiguration;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import com.movieland.entity.Review;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {TestSecurityConfiguration.class, RedisConfiguration.class, H2ServerConfiguration.class})
@ActiveProfiles({"test", "multi"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;
    private static final int MOVIE_ID = 1;
    private static final int ZERO_ID = 0;
    private static final int NEGATIVE_ID = 0;
    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/api/v1/movies/";

    @Test
    @Order(1)
    @SuppressWarnings("squid:S5961") // There are 26 assertions, instead recommend 25
    @DisplayName("Test - find movie by id, using singe enrichment")
    void givenMovie_whenFindMovieById_thenReturnMovie() {
        MovieFullInfoDto expectedMovie = createMovieDto();

        ResponseEntity<MovieFullInfoDto> response = restTemplate.getForEntity(generatePath(MOVIE_ID), MovieFullInfoDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        MovieFullInfoDto actualMovie = response.getBody();

        // Assert movies attributes
        Assertions.assertEquals(expectedMovie.getNameNative(), actualMovie.getNameNative());
        Assertions.assertEquals(expectedMovie.getNameRussian(), actualMovie.getNameRussian());
        Assertions.assertEquals(expectedMovie.getYearOfRelease(), actualMovie.getYearOfRelease());
        Assertions.assertEquals(expectedMovie.getDescription(), actualMovie.getDescription());
        Assertions.assertEquals(expectedMovie.getRating(), actualMovie.getRating());
        Assertions.assertEquals(expectedMovie.getPrice(), actualMovie.getPrice());
        Assertions.assertEquals(expectedMovie.getPicturePath(), actualMovie.getPicturePath());

        // Assert movies countries
        List<Country> countries = actualMovie.getCountries();
        int expectedCountriesSize = 3;

        Assertions.assertNotNull(countries);
        Assertions.assertEquals(expectedCountriesSize, countries.size());

        Assertions.assertEquals("США", countries.get(0).getName());
        Assertions.assertEquals("Италия", countries.get(1).getName());
        Assertions.assertEquals("Франция", countries.get(2).getName());

        // Assert movies genres
        List<Genre> genres = actualMovie.getGenres();
        int expectedGenresSize = 4;

        Assertions.assertNotNull(genres);
        Assertions.assertEquals(expectedGenresSize, genres.size());

        Assertions.assertEquals("Комедия", genres.get(0).getName());
        Assertions.assertEquals("Фантастика", genres.get(1).getName());
        Assertions.assertEquals("Приключения", genres.get(2).getName());
        Assertions.assertEquals("Семейный", genres.get(3).getName());

        // Assert movies reviews
        List<Review> reviews = actualMovie.getReviews();
        int expectedReviewsSize = 4;

        Assertions.assertNotNull(reviews);
        Assertions.assertEquals(expectedReviewsSize, reviews.size());

        Assertions.assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, что только так и должно быть...", reviews.get(0).getText());
        Assertions.assertEquals("Кино это является, безусловно, «со знаком качества»...", reviews.get(1).getText());
        Assertions.assertEquals("Перестал удивляться тому, что этот фильм занимает сплошь первые места во всевозможных кино рейтингах...", reviews.get(2).getText());
        Assertions.assertEquals("Много еще можно сказать об этом шедевре...", reviews.get(3).getText());
    }

    @Test
    @Order(2)
    @DisplayName("Test - return bad request if id 0")
    void givenZeroId_whenFindById_thenReturnBadRequest() {

        ResponseEntity<MovieFullInfoDto> response =
                restTemplate.getForEntity(generatePath(ZERO_ID), MovieFullInfoDto.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        MovieFullInfoDto actualMovie = response.getBody();

        assert actualMovie != null;
        Assertions.assertNull(actualMovie.getNameNative());
        Assertions.assertNull(actualMovie.getNameRussian());
        Assertions.assertEquals(0, actualMovie.getYearOfRelease());
        Assertions.assertNull(actualMovie.getDescription());
        Assertions.assertNull(actualMovie.getRating());
        Assertions.assertNull(actualMovie.getPrice());
        Assertions.assertNull(actualMovie.getPicturePath());
        Assertions.assertNull(actualMovie.getCountries());
        Assertions.assertNull(actualMovie.getGenres());
        Assertions.assertNull(actualMovie.getReviews());
    }

    @Test
    @Order(3)
    @DisplayName("Test - return bad request if id is negative")
    void givenNegativeId_whenFindById_thenReturnBadRequest() throws Exception {

        ResponseEntity<MovieFullInfoDto> response =
                restTemplate.getForEntity(generatePath(NEGATIVE_ID), MovieFullInfoDto.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        MovieFullInfoDto actualMovie = response.getBody();

        assert actualMovie != null;
        Assertions.assertNull(actualMovie.getNameNative());
        Assertions.assertNull(actualMovie.getNameRussian());
        Assertions.assertEquals(0, actualMovie.getYearOfRelease());
        Assertions.assertNull(actualMovie.getDescription());
        Assertions.assertNull(actualMovie.getRating());
        Assertions.assertNull(actualMovie.getPrice());
        Assertions.assertNull(actualMovie.getPicturePath());
        Assertions.assertNull(actualMovie.getCountries());
        Assertions.assertNull(actualMovie.getGenres());
        Assertions.assertNull(actualMovie.getReviews());
    }

    @Test
    @Order(4)
    @DisplayName("Test - return not found if movie does not exist")
    void givenNotExistingId_whenFindById_thenReturnNotFound() {
        ResponseEntity<MovieFullInfoDto> response =
                restTemplate.getForEntity(generatePath(Integer.MAX_VALUE), MovieFullInfoDto.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        MovieFullInfoDto actualMovie = response.getBody();

        assert actualMovie != null;
        Assertions.assertNull(actualMovie.getNameNative());
        Assertions.assertNull(actualMovie.getNameRussian());
        Assertions.assertEquals(0, actualMovie.getYearOfRelease());
        Assertions.assertNull(actualMovie.getDescription());
        Assertions.assertNull(actualMovie.getRating());
        Assertions.assertNull(actualMovie.getPrice());
        Assertions.assertNull(actualMovie.getPicturePath());
        Assertions.assertNull(actualMovie.getCountries());
        Assertions.assertNull(actualMovie.getGenres());
        Assertions.assertNull(actualMovie.getReviews());
    }

    private String generatePath(int id) {
        String template = "%s%d%s%d";
        return String.format(template, LOCAL_HOST, port, PATH, id);
    }

    private MovieFullInfoDto createMovieDto() {
        return MovieFullInfoDto.builder()
                .nameRussian("Пятьдесят оттенков черного")
                .nameNative("Fifty Shades of Black")
                .yearOfRelease(2016)
                .description("Феномен успеха эротической мелодрамы «Пятьдесят оттенков серого» многим не дает покоя: одни удивляются, другие возмущаются, а третьи открыто или тайно ждут не дождутся продолжения нашумевшей истории...")
                .rating(1.0)
                .price(100.0)
                .picturePath("https://uafilm.pro/3820-pyatdesyat-vdtnkv-chornogo.html")
                .build();
    }
}