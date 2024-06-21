package com.movieland.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

/**
 * Configuration class to exclude Redis autoconfiguration for testing environments.
 * This class disables the default autoconfiguration for Redis that Spring Boot typically enables.
 * By excluding {@link RedisAutoConfiguration} and {@link RedisRepositoriesAutoConfiguration}
 */

@Configuration
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class RedisExclusionConfiguration {
    // This class serves as a configuration modifier and contains no methods or fields.
}
