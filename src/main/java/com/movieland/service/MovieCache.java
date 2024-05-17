package com.movieland.service;

import com.movieland.common.Currency;
import com.movieland.dto.MovieFullInfoDto;
import org.springframework.scheduling.annotation.Scheduled;

public interface MovieCache {
    MovieFullInfoDto getFromCache(int movieId, Currency currency);

    MovieFullInfoDto saveToCache(int movieId, MovieFullInfoDto movie);

    @Scheduled(cron = "${cron.each.midnight}")
    void cleanUpCache();
}
