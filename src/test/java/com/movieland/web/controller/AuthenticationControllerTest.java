package com.movieland.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.movieland.entity.User;
import com.movieland.repository.UserRepository;
import com.movieland.service.impl.JwtService;
import com.movieland.web.AbstractBaseITest;
import jakarta.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.util.NestedServletException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthenticationControllerTest extends AbstractBaseITest {

    private static final String LOGIN = "/api/v1/login";
    private static final String LOGOUT = "/api/v1/logout";
    private static final String AUTH_REQUEST_JSON = "{ \"email\": \"darlene.edwards15@example.com\", \"password\": \"bricks\" }";
    private static final String AUTH_REQUEST_BAD_CREDENTIALS_JSON = "{ \"email\": \"test@example.com\", \"password\": \"test\" }";


    static GenericContainer redis;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeAll
    static void beforeAll() {
        redis = new GenericContainer(DockerImageName.parse("redis:7.2.4"))
                .withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }

    @AfterAll
    static void stopRedis() {
        redis.stop();
    }

    @Test
    @DataSet(value = "datasets/movies_genres_countries_reviews_users.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testLoginReceiveTokenAndNickName() throws Exception {

        mockMvc.perform(post(LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AUTH_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("Дарлин Эдвардс"))
                .andExpect(jsonPath("$.uuid").isNotEmpty());
    }

    @Test
    @DataSet(value = "datasets/movies_genres_countries_reviews_users.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testLoginWithWrongCredentialsReceiveBadRequest() throws Exception {
        assertThrows(ServletException.class, () -> mockMvc.perform(post(LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AUTH_REQUEST_BAD_CREDENTIALS_JSON)));

    }

    @Test
    @DataSet(value = "datasets/movies_genres_countries_reviews_users.yml",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void testLogoutVerifyTokenAddedToCache() throws Exception {
        User user = userRepository.findById(3).get();
        String token = jwtService.generateToken(user);
        String authHeader = "Bearer " + token;

        mockMvc.perform(delete(LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON).header("AUTHORIZATION", authHeader)
                        .content(AUTH_REQUEST_JSON))
                .andExpect(status().isOk());

        boolean cachedToken = redisTemplate.hasKey(token);
        long tokenTimeToLive = redisTemplate.getExpire(token, TimeUnit.MINUTES);

        Assertions.assertTrue(cachedToken);
        Assertions.assertEquals(119, tokenTimeToLive);

    }

}
