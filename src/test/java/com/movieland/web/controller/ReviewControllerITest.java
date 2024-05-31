//package com.movieland.web.controller;
//
//import com.github.database.rider.core.api.dataset.DataSet;
//import com.movieland.entity.User;
//import com.movieland.repository.UserRepository;
//import com.movieland.service.impl.JwtService;
//import com.movieland.web.AbstractBaseITest;
//import com.vladmihalcea.sql.SQLStatementCountValidator;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import static com.vladmihalcea.sql.SQLStatementCountValidator.assertInsertCount;
//import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//class ReviewControllerITest extends AbstractBaseITest {
//
//    //ENDPOINTS
//    private static final String REVIEW_URI = "/api/v1/review";
//
//    //CONTENT
//    private static final String REVIEW_CONTENT_JSON = "{ \"movieId\": 1, \"text\": \"Amazing movie!\" }";
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Test
//    @DataSet(value = "datasets/to_post_review.yml",
//            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
//    public void postReviewAsUserReceiveResponseOkAndInsertCountOne() throws Exception {
//        User user = userRepository.findById(3).get();
//        String token = jwtService.generateToken(user);
//        String authHeader = "Bearer " + token;
//
//        SQLStatementCountValidator.reset();
//        mockMvc.perform(post(REVIEW_URI)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("AUTHORIZATION", authHeader)
//                        .content(REVIEW_CONTENT_JSON))
//                .andExpect(status().isOk());
//        assertSelectCount(2);
//        assertInsertCount(1);
//    }
//
//    @Test
//    @DataSet(value = "datasets/to_post_review.yml",
//            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
//    public void postReviewAsGuestReceiveResponseBadRequest() throws Exception {
//
//        SQLStatementCountValidator.reset();
//        mockMvc.perform(post(REVIEW_URI)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(REVIEW_CONTENT_JSON))
//                .andExpect(status().isBadRequest());
//        assertSelectCount(0);
//        assertInsertCount(0);
//
//    }
//
//
//}
