package com.movieland.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.movieland.web.AbstractBaseITest;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertInsertCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MovieControllerITest extends AbstractBaseITest {

    //ENDPOINTS
    private static final String ALL_MOVIES_API = "/api/v1/movies";
    private static final String ALL_MOVIES_RATING_DESC_API = "/api/v1/movies?rating=desc";
    private static final String ALL_MOVIES_PRICE_ASC_API = "/api/v1/movies?price=asc";
    private static final String ALL_MOVIES_PRICE_DESC_API = "/api/v1/movies?price=desc";
    private static final String ALL_MOVIES_BY_GENRE_CRIME_API = "/api/v1/movies/genre/2";
    private static final String ALL_MOVIES_BY_GENRE_DRAMA_PRICE_ASC_API = "/api/v1/movies/genre/1?price=asc";
    private static final String ALL_MOVIES_BY_GENRE_DRAMA_PRICE_DESC_API = "/api/v1/movies/genre/1?price=desc";
    private static final String ALL_MOVIES_BY_GENRE_DRAMA_RATING_DESC_API = "/api/v1/movies/genre/1?rating=desc";
    private static final String FIND_MOVIE_BY_ID_API = "/api/v1/movies/1";
    private static final String MOVIE_CONTENT_JSON = "{\n" +
                                                     "     \"nameRussian\": \"Побег из Шоушенка\",\n" +
                                                     "     \"nameNative\": \"The Shawshank Redemption\",\n" +
                                                     "     \"yearOfRelease\": \"1994\",\n" +
                                                     "     \"description\": \"Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.\",\n" +
                                                     "     \"price\": 123.45,\n" +
                                                     "     \"rating\": 8.9,\n" +
                                                     "     \"picturePath\": \"https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg\",\n" +
                                                     "     \"countries\": [1,2],\n" +
                                                     "     \"genres\": [1,2,3]\n" +
                                                     "}";


    //JSONs
    private static final String ALL_MOVIES_JSON = "response/movies/all-movies.json";
    private static final String ALL_MOVIES_RATING_DESC_JSON = "response/movies/all-movies-rating-desc.json";
    private static final String ALL_MOVIES_PRICE_ASC_JSON = "response/movies/all-movies-price-asc.json";
    private static final String ALL_MOVIES_PRICE_DESC_JSON = "response/movies/all-movies-price-desc.json";
    private static final String ALL_MOVIES_BY_GENRE_CRIME_JSON = "response/movies/all-crime-movies.json";
    private static final String ALL_MOVIES_BY_GENRE_DRAMA_PRICE_ASC_JSON = "response/movies/all-drama-movies-price-asc.json";
    private static final String ALL_MOVIES_BY_GENRE_DRAMA_PRICE_DESC_JSON = "response/movies/all-drama-movies-price-desc.json";
    private static final String ALL_MOVIES_BY_GENRE_DRAMA_RATING_DESC_JSON = "response/movies/all-drama-movies-rating-desc.json";
    private static final String FIND_MOVIE_BY_ID_ONE_JSON = "response/movies/movie-by-id-1.json";


    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void getAllMovies() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_JSON)));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testGetAllMoviesRatingDesc() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_RATING_DESC_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_RATING_DESC_JSON), true));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testGetAllMoviesPriceAsc() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_PRICE_ASC_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_PRICE_ASC_JSON), true));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testGetAllMoviesPriceDesc() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_PRICE_DESC_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_PRICE_DESC_JSON), true));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testGetThreeRandomMovies() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_PRICE_DESC_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_PRICE_DESC_JSON)));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testMoviesByGenre() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_BY_GENRE_CRIME_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_BY_GENRE_CRIME_JSON)));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testMoviesByGenrePriceAscending() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_BY_GENRE_DRAMA_PRICE_ASC_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_BY_GENRE_DRAMA_PRICE_ASC_JSON), true));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testMoviesByGenrePriceDescending() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_BY_GENRE_DRAMA_PRICE_DESC_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_BY_GENRE_DRAMA_PRICE_DESC_JSON), true));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testMoviesByGenreRatingDescending() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(ALL_MOVIES_BY_GENRE_DRAMA_RATING_DESC_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(ALL_MOVIES_BY_GENRE_DRAMA_RATING_DESC_JSON), true));
        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movies_genres_countries_reviews_users.yml",
            cleanAfter = true,
            cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testGetMovieById() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(get(FIND_MOVIE_BY_ID_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString(FIND_MOVIE_BY_ID_ONE_JSON), true));
        assertSelectCount(3);
    }

    @Test
    @DataSet(value = "datasets/to_save_movie.yml",
            cleanAfter = true,
            cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testSaveMovieReceiveOk() throws Exception {
        SQLStatementCountValidator.reset();
        mockMvc.perform(post(ALL_MOVIES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MOVIE_CONTENT_JSON))
                .andExpect(status().isOk());

        assertSelectCount(3);
        assertInsertCount(6);

    }
}
