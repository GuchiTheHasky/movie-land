package com.movieland.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.mock;

/**
 * Configuration for Redis used in testing. It provides a mocked RedisTemplate to avoid interacting with a real Redis instance.
 */
@TestConfiguration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, String> testRedisTemplate() {
        return mock(RedisTemplate.class);
    }
}
