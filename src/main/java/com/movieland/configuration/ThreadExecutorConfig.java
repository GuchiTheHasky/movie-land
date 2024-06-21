package com.movieland.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class ThreadExecutorConfig {

    @Getter
    private static AtomicInteger totalThreadsCount = new AtomicInteger(0);

    @Bean
    public ThreadFactory daemonThreadFactory() {
        return runnable -> {
            Thread thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        log.error("Error task executing:", e);
                        Thread.currentThread().interrupt();
                    }
                }
                log.info("FINALLY U ARE HERE > > > Thread {} is finished.", Thread.currentThread().getName());
            });
            thread.setDaemon(true);
            totalThreadsCount.incrementAndGet();
            return thread;
        };
    }

    @Bean
    public ExecutorService cachedDaemonThreadPool(@Qualifier("daemonThreadFactory") ThreadFactory threadFactory) {
        return Executors.newCachedThreadPool(threadFactory);
    }

}
