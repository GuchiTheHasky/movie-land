package com.movieland.web;

import com.github.database.rider.spring.api.DBRider;
import com.movieland.config.DataSourceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@DBRider
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@Testcontainers
@Import(DataSourceWrapper.class)
public class AbstractBaseITest {

    @Autowired
    protected MockMvc mockMvc;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16.2");

    protected String getResponseAsString(String jsonPath) {
        URL resource = getClass().getClassLoader().getResource(jsonPath);
        try {
            return FileUtils.readFileToString(new File(resource.toURI()), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Unable to find file: " + jsonPath, e);
        }
    }


}
