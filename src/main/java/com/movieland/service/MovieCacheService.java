package com.movieland.service;

import com.movieland.common.Currency;
import com.movieland.dto.MovieFullInfoDto;
import org.springframework.scheduling.annotation.Scheduled;

public interface MovieCacheService {
    MovieFullInfoDto findInCache(int movieId, Currency currency);

    MovieFullInfoDto cacheAndReturn(int movieId, MovieFullInfoDto movie);

    void cleanUpCache();
}
