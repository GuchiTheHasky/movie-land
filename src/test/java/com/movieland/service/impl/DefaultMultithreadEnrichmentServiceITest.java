package com.movieland.service.impl;

import com.movieland.config.H2ServerConfiguration;
import com.movieland.config.RedisConfiguration;
import com.movieland.config.TestSecurityConfiguration;
import com.movieland.configuration.ThreadExecutorConfig;
import com.movieland.entity.*;
import com.movieland.exception.MovieNotFoundException;
import com.movieland.mapper.MovieMapper;
import com.movieland.repository.CountryRepository;
import com.movieland.repository.GenreRepository;
import com.movieland.repository.MovieRepository;
import com.movieland.repository.ReviewRepository;
import com.movieland.repository.projection.MovieProjection;
import com.movieland.service.EnrichmentService;
import lombok.extern.slf4j.Slf4j;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestSecurityConfiguration.class, RedisConfiguration.class, H2ServerConfiguration.class})
@ActiveProfiles({"test", "multi"})
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class DefaultMultithreadEnrichmentServiceITest {
    private static final String[] EXPECTED_COUNTRIES = {"США", "Италия", "Франция"};
    private static final String[] EXPECTED_GENRES = {"Комедия", "Фантастика", "Приключения", "Семейный"};
    private static final String[] EXPECTED_REVIEWS = {
            "Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, что только так и должно быть...",
            "Кино это является, безусловно, «со знаком качества»...",
            "Перестал удивляться тому, что этот фильм занимает сплошь первые места во всевозможных кино рейтингах...",
            "Много еще можно сказать об этом шедевре..."
    };

    @Autowired
    private EnrichmentService enrichmentService;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    @Autowired
    private MovieMapper movieMapper;

    private LogCaptor logCaptor;


    @BeforeEach
    void setUp() {
        logCaptor = LogCaptor.forClass(DefaultMultithreadEnrichmentService.class);
    }

    //    @BeforeAll
    //    public static void initTest() throws SQLException {
    //        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
    //    }

    @Test
    @Order(1)
    @DisplayName("Test - should enriched countries in movie")
    void enrichMovieCountriesInfo() {
        // given Movie and daemon threads count
        Movie movie = findMovie();
        int expectedThreadCount = 1;

        assertNotNull(movie);

        // Assert that the genres, countries, and reviews lists are empty
        assertNull(movie.getCountries());
        assertNull(movie.getGenres());
        assertNull(movie.getReviews());

        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.COUNTRY);

        // Assert daemon threads count
        assertEquals(expectedThreadCount, ThreadExecutorConfig.getTotalThreadsCount().get());

        // Assert logs
        assertTrue(logCaptor.getInfoLogs().contains("Enrichment tasks invoked successfully"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: COUNTRY"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: GENRE"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: REVIEW"));

        // Assert movie not null after enrichment
        assertNotNull(enrichedMovie);

        // Assert that the enriched movie has non-empty genres, countries, and reviews lists
        assertNotNull(enrichedMovie.getCountries());
        assertNull(movie.getGenres());
        assertNull(movie.getReviews());

        List<Country> enrichedCountries = enrichedMovie.getCountries();
        // Assert that enriched countries match expected countries
        assertEquals(EXPECTED_COUNTRIES[0], enrichedCountries.get(0).getName());
        assertEquals(EXPECTED_COUNTRIES[1], enrichedCountries.get(1).getName());
        assertEquals(EXPECTED_COUNTRIES[2], enrichedCountries.get(2).getName());
    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("Test - should enriched countries, genres & reviews in movie")
    void enrichMovieCountriesGenresInfo() {
        // given Movie and daemon threads count
        Movie movie = findMovie();
        int expectedThreadCount = 2;

        assertNotNull(movie);
        // Assert that the genres, countries, and reviews lists are empty
        assertNull(movie.getGenres());
        assertNull(movie.getCountries());
        assertNull(movie.getReviews());

        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.COUNTRY, EnrichmentType.GENRE);

        // Assert daemon threads count
        assertEquals(expectedThreadCount, ThreadExecutorConfig.getTotalThreadsCount().get());

        // Assert logs
        assertTrue(logCaptor.getInfoLogs().contains("Enrichment tasks invoked successfully"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: COUNTRY"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: GENRE"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: REVIEW"));

        // Assert movie not null after enrichment
        assertNotNull(enrichedMovie);
        // Assert that the enriched movie has non-empty genres, countries, and reviews lists
        assertNotNull(enrichedMovie.getGenres());
        assertNotNull(enrichedMovie.getCountries());
        assertNull(enrichedMovie.getReviews());

        List<Country> enrichedCountries = enrichedMovie.getCountries();
        // Assert that enriched countries match expected countries
        assertEquals(EXPECTED_COUNTRIES[0], enrichedCountries.get(0).getName());
        assertEquals(EXPECTED_COUNTRIES[1], enrichedCountries.get(1).getName());
        assertEquals(EXPECTED_COUNTRIES[2], enrichedCountries.get(2).getName());

        List<Genre> enrichedGenres = enrichedMovie.getGenres();
        // Assert that enriched genres match expected genres
        assertEquals(EXPECTED_GENRES[0], enrichedGenres.get(0).getName());
        assertEquals(EXPECTED_GENRES[1], enrichedGenres.get(1).getName());
        assertEquals(EXPECTED_GENRES[2], enrichedGenres.get(2).getName());
        assertEquals(EXPECTED_GENRES[3], enrichedGenres.get(3).getName());
    }

    @Test
    @Order(3)
    @DisplayName("Test - should enriched countries, genres & reviews in movie")
    void enrichMovieCountriesGenresReviewsInfo() {
        Movie movie = findMovie();
        int expectedThreadCount = 3;

        assertNotNull(movie);
        // Assert that the genres, countries, and reviews lists are empty
        assertNull(movie.getGenres());
        assertNull(movie.getCountries());
        assertNull(movie.getReviews());

        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.values());

        // Assert daemon threads count
        assertEquals(expectedThreadCount, ThreadExecutorConfig.getTotalThreadsCount().get());

        // Assert logs
        assertTrue(logCaptor.getInfoLogs().contains("Enrichment tasks invoked successfully"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: COUNTRY"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: GENRE"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: REVIEW"));

        // Assert movie not null after enrichment
        assertNotNull(enrichedMovie);
        // Assert that the enriched movie has non-empty genres, countries, and reviews lists
        assertNotNull(enrichedMovie.getGenres());
        assertNotNull(enrichedMovie.getCountries());
        assertNotNull(enrichedMovie.getReviews());

        List<Country> enrichedCountries = enrichedMovie.getCountries();
        // Assert that enriched countries match expected countries
        assertEquals(EXPECTED_COUNTRIES[0], enrichedCountries.get(0).getName());
        assertEquals(EXPECTED_COUNTRIES[1], enrichedCountries.get(1).getName());
        assertEquals(EXPECTED_COUNTRIES[2], enrichedCountries.get(2).getName());

        List<Genre> enrichedGenres = enrichedMovie.getGenres();
        // Assert that enriched genres match expected genres
        assertEquals(EXPECTED_GENRES[0], enrichedGenres.get(0).getName());
        assertEquals(EXPECTED_GENRES[1], enrichedGenres.get(1).getName());
        assertEquals(EXPECTED_GENRES[2], enrichedGenres.get(2).getName());
        assertEquals(EXPECTED_GENRES[3], enrichedGenres.get(3).getName());

        List<Review> enrichedReviews = enrichedMovie.getReviews();
        // Assert that enriched reviews match expected reviews
        assertEquals(EXPECTED_REVIEWS[0], enrichedReviews.get(0).getText());
        assertEquals(EXPECTED_REVIEWS[1], enrichedReviews.get(1).getText());
        assertEquals(EXPECTED_REVIEWS[2], enrichedReviews.get(2).getText());
        assertEquals(EXPECTED_REVIEWS[3], enrichedReviews.get(3).getText());
    }

    @Test
    @Order(4)
    @Transactional
    @DisplayName("Test - should enriched genres in movie")
    void enrichMovieGenresInfo() {
        Movie movie = findMovie();

        assertNotNull(movie);
        // Assert that the genres, countries, and reviews lists are empty
        assertNull(movie.getGenres());
        assertNull(movie.getCountries());
        assertNull(movie.getReviews());

        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.GENRE);

        // Assert logs
        assertTrue(logCaptor.getInfoLogs().contains("Enrichment tasks invoked successfully"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: COUNTRY"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: GENRE"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: REVIEW"));

        assertNotNull(enrichedMovie);
        // Assert that the enriched movie has non-empty genres, countries, and reviews lists
        assertNotNull(enrichedMovie.getGenres());
        assertNull(movie.getCountries());
        assertNull(movie.getReviews());

        List<Genre> enrichedGenres = enrichedMovie.getGenres();
        // Assert that enriched genres match expected genres
        assertEquals(EXPECTED_GENRES[0], enrichedGenres.get(0).getName());
        assertEquals(EXPECTED_GENRES[1], enrichedGenres.get(1).getName());
        assertEquals(EXPECTED_GENRES[2], enrichedGenres.get(2).getName());
        assertEquals(EXPECTED_GENRES[3], enrichedGenres.get(3).getName());
    }

    @Test
    @Order(5)
    @Transactional
    @DisplayName("Test - should enriched reviews in movie")
    void enrichMovieReviewsInfo() {
        Movie movie = findMovie();

        assertNotNull(movie);
        // Assert that the genres, countries, and reviews lists are empty
        assertNull(movie.getGenres());
        assertNull(movie.getCountries());
        assertNull(movie.getReviews());

        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.REVIEW);

        // Assert logs
        assertTrue(logCaptor.getInfoLogs().contains("Enrichment tasks invoked successfully"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: COUNTRY"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: GENRE"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: REVIEW"));

        assertNotNull(enrichedMovie);
        // Assert that the enriched movie has non-empty genres, countries, and reviews lists
        assertNull(enrichedMovie.getGenres());
        assertNull(enrichedMovie.getCountries());
        assertNotNull(enrichedMovie.getReviews());

        List<Review> enrichedReviews = enrichedMovie.getReviews();
        // Assert that enriched reviews match expected reviews
        assertEquals(EXPECTED_REVIEWS[0], enrichedReviews.get(0).getText());
        assertEquals(EXPECTED_REVIEWS[1], enrichedReviews.get(1).getText());
        assertEquals(EXPECTED_REVIEWS[2], enrichedReviews.get(2).getText());
        assertEquals(EXPECTED_REVIEWS[3], enrichedReviews.get(3).getText());
    }

    @Test
    @Order(6)
    @Transactional
    @DisplayName("Test - should enriched countries & reviews in movie")
    void enrichMovieCountriesReviewsInfo() {
        Movie movie = findMovie();

        assertNotNull(movie);
        // Assert that the genres, countries, and reviews lists are empty
        assertNull(movie.getGenres());
        assertNull(movie.getCountries());
        assertNull(movie.getReviews());

        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.COUNTRY, EnrichmentType.REVIEW);

        // Assert logs
        assertTrue(logCaptor.getInfoLogs().contains("Enrichment tasks invoked successfully"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: COUNTRY"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: GENRE"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: REVIEW"));

        assertNotNull(enrichedMovie);
        // Assert that the enriched movie has non-empty genres, countries, and reviews lists
        assertNull(enrichedMovie.getGenres());
        assertNotNull(enrichedMovie.getCountries());
        assertNotNull(enrichedMovie.getReviews());

        List<Country> enrichedCountries = enrichedMovie.getCountries();
        // Assert that enriched countries match expected countries
        assertEquals(EXPECTED_COUNTRIES[0], enrichedCountries.get(0).getName());
        assertEquals(EXPECTED_COUNTRIES[1], enrichedCountries.get(1).getName());
        assertEquals(EXPECTED_COUNTRIES[2], enrichedCountries.get(2).getName());

        List<Review> enrichedReviews = enrichedMovie.getReviews();
        // Assert that enriched reviews match expected reviews
        assertEquals(EXPECTED_REVIEWS[0], enrichedReviews.get(0).getText());
        assertEquals(EXPECTED_REVIEWS[1], enrichedReviews.get(1).getText());
        assertEquals(EXPECTED_REVIEWS[2], enrichedReviews.get(2).getText());
        assertEquals(EXPECTED_REVIEWS[3], enrichedReviews.get(3).getText());
    }

    @Test
    @Order(7)
    @Transactional
    @DisplayName("Test - should enriched genres & reviews in movie")
    void enrichMovieGenresReviewsInfo() {
        Movie movie = findMovie();

        assertNotNull(movie);
        // Assert that the genres, countries, and reviews lists are empty
        assertNull(movie.getGenres());
        assertNull(movie.getCountries());
        assertNull(movie.getReviews());

        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.GENRE, EnrichmentType.REVIEW);

        // Assert logs
        assertTrue(logCaptor.getInfoLogs().contains("Enrichment tasks invoked successfully"));
        assertFalse(logCaptor.getInfoLogs().contains("Data fetched successfully for type: COUNTRY"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: GENRE"));
        assertTrue(logCaptor.getInfoLogs().contains("Data fetched successfully for type: REVIEW"));

        assertNotNull(enrichedMovie);
        // Assert that the enriched movie has non-empty genres, countries, and reviews lists
        assertNotNull(enrichedMovie.getGenres());
        assertNull(enrichedMovie.getCountries());
        assertNotNull(enrichedMovie.getReviews());

        List<Genre> enrichedGenres = enrichedMovie.getGenres();
        // Assert that enriched genres match expected genres
        assertEquals(EXPECTED_GENRES[0], enrichedGenres.get(0).getName());
        assertEquals(EXPECTED_GENRES[1], enrichedGenres.get(1).getName());
        assertEquals(EXPECTED_GENRES[2], enrichedGenres.get(2).getName());
        assertEquals(EXPECTED_GENRES[3], enrichedGenres.get(3).getName());

        List<Review> enrichedReviews = enrichedMovie.getReviews();
        // Assert that enriched reviews match expected reviews
        assertEquals(EXPECTED_REVIEWS[0], enrichedReviews.get(0).getText());
        assertEquals(EXPECTED_REVIEWS[1], enrichedReviews.get(1).getText());
        assertEquals(EXPECTED_REVIEWS[2], enrichedReviews.get(2).getText());
        assertEquals(EXPECTED_REVIEWS[3], enrichedReviews.get(3).getText());
    }

    //    @Test
    //    @Order(8)
    //    @Transactional
    //    void testReturnMovieWithoutAdditionalInfoIfThreadStateIsDead() {
    //        ThreadExecutorConfig.ThreadState.setDead(true);
    //        Movie movie = findMovie();
    //        int expectedThreadCount = 0;
    //        ThreadExecutorConfig.ThreadState.resetCount();
    //
    //        Movie enrichedMovie = enrichmentService.enrichAdditionalInfo(movie, EnrichmentType.values());
    //
    //        assertTrue(logCaptor.getWarnLogs().contains("Stopping additional info enrichment due to previous error"));
    //        assertEquals(expectedThreadCount, ThreadExecutorConfig.ThreadState.getThreadCount());
    //        assertSame(movie, enrichedMovie);
    //        // reset changes
    //        ThreadExecutorConfig.ThreadState.setDead(false);
    //    }

    private Movie findMovie() {
        MovieProjection movieProjection = movieRepository.findByIdProjection(1).orElseThrow(() -> new MovieNotFoundException(1));
        ;
        Movie movie = movieRepository.findById(1).get();
        System.out.println();
        return movieMapper.toMovie(movieProjection);
    }


    //    @Test
    //        void t() {
    //
    //            System.out.println("***");
    //            System.out.println("***");
    //            System.out.println("***");
    //            List<Movie> movies = movieRepository.findAll();
    //            System.out.println(movies.size());
    //
    //            List<Country> countries = countryRepository.findAll();
    //            System.out.println(countries.size());
    //            List<Genre> genres = genreRepository.findAll();
    //            System.out.println(genres.size());
    //            List<Review> reviews = reviewRepository.findAll();
    //            System.out.println(reviews.size());
    //
    //            System.out.println("***");
    //            System.out.println("***");
    //            System.out.println("***");
    //
    //           while (true) {
    //               new Thread().start();
    //           }
    //        }

}