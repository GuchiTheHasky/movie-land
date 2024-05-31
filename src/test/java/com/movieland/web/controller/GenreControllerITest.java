//package com.movieland.web.controller;
//
//import com.github.database.rider.core.api.dataset.DataSet;
//import com.movieland.web.AbstractBaseITest;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//public class GenreControllerITest extends AbstractBaseITest {
//
//    private static final String GENRE_API = "/api/v1/genre";
//    public static final String MOCK_GENRES_PATH = "response/genres/all-genres.json";
//
//    @Test
//    @DataSet(value = "datasets/movies_genres.yml",
//            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
//    public void getAllGenres() throws Exception {
//
//        mockMvc.perform(get(GENRE_API)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(getResponseAsString(MOCK_GENRES_PATH)));
//    }
//
//}
