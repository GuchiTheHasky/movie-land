package com.movieland.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Configuration
public class ThreadExecutorConfig {

    @Bean
    public ThreadFactory threadFactory() {
        return runnable -> {
            Thread thread = new Thread(() -> {
                Callable<Object> callable = Executors.callable(runnable, List.of());

                try {
                    callable.call();
                } catch (Exception e) {
                    log.error("Error task executing:", e);
                }

            });
            thread.setDaemon(true);
            return thread;
        };
    }

    @Bean
    public ExecutorService cachedDaemonThreadPool(ThreadFactory threadFactory) {
        return Executors.newCachedThreadPool(threadFactory);
    }
}


