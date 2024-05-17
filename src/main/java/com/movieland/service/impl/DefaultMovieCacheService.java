package com.movieland.service.impl;

import com.movieland.common.Currency;
import com.movieland.common.annotations.Cache;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.service.CurrencyConverterService;
import com.movieland.service.MovieCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Cache
@RequiredArgsConstructor
public class DefaultMovieCacheService implements MovieCacheService {

    private final CurrencyConverterService currencyConverterService;

    private final ConcurrentHashMap<Integer, SoftReference<MovieFullInfoDto>> movieCache = new ConcurrentHashMap<>();

    @Override
    public MovieFullInfoDto getFromCache(int movieId, Currency currency) {
        SoftReference<MovieFullInfoDto> reference = movieCache.get(movieId);

        if (reference != null) {
            MovieFullInfoDto cachedMovie = reference.get();
                if (currency != null) {
                    double price = currencyConverterService.convertFromUah(cachedMovie.getPrice(), currency);
                    cachedMovie.setPrice(price);
                }
                return cachedMovie;
        }
        return null;
    }

    @Override
    public MovieFullInfoDto handleCacheIfAbsent(int movieId, MovieFullInfoDto movie) {
        movieCache.putIfAbsent(movieId, new SoftReference<>(movie));
        return movie;
    }

    @Override
    @Scheduled(cron = "${cache.movie.cron}")
    public void cleanUpCache() {
        log.info("Starting cache cleanup...");
        movieCache.entrySet().removeIf(entry -> entry.getValue().get() == null);
        log.info("Cache cleanup completed.");
    }
}
