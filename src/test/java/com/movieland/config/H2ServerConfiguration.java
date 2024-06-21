package com.movieland.config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

/**
 * Configuration class for H2 database servers used in testing.
 * This setup includes both TCP and Web server configurations running on random ports to avoid port conflicts.
 */
@Slf4j
@TestConfiguration
public class H2ServerConfiguration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        log.info("Starting H2 server...");
        Server server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "0");
        log.info("H2 server started.");
        return server;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        log.info("Starting H2 web-server...");
        Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "0");
        log.info("H2 web-server started. Available on: {}", server.getURL());
        return server;
    }
}
