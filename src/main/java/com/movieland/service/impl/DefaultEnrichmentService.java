package com.movieland.service.impl;

import com.movieland.entity.*;
import com.movieland.repository.CountryRepository;
import com.movieland.repository.GenreRepository;
import com.movieland.repository.ReviewRepository;
import com.movieland.service.EnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@Profile("single")
@RequiredArgsConstructor
public class DefaultEnrichmentService implements EnrichmentService {

    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Movie enrichAdditionalInfo(Movie movie, EnrichmentType... enrichmentTypes) {
        log.info("========================");
        log.info("primary");
        log.info("========================");
        Map<EnrichmentType, List<?>> map = getAdditionalList(movie, enrichmentTypes);

        List<Genre> genres = (List<Genre>) getAdditionalInfoByType(EnrichmentType.GENRE, map);
        List<Country> countries = (List<Country>) getAdditionalInfoByType(EnrichmentType.COUNTRY, map);
        List<Review> reviews = (List<Review>) getAdditionalInfoByType(EnrichmentType.REVIEW, map);

        return enrichInfo(movie, genres, countries, reviews);
    }

    private Map<EnrichmentType, List<?>> getAdditionalList(Movie movie, EnrichmentType[] enrichmentTypes) {
        Map<EnrichmentType, List<?>> map = new HashMap<>();

        Arrays.stream(enrichmentTypes).forEach(type -> {
                    switch (type) {
                        case GENRE:
                            map.put(type, genreRepository.findByMovieId(movie.getId()));
                            log.info("Found genres for movie: {}", movie.getId());
                            break;
                        case COUNTRY:
                            map.put(type, countryRepository.findByMovieId(movie.getId()));
                            log.info("Found countries for movie: {}", movie.getId());
                            break;
                        case REVIEW:
                            map.put(type, reviewRepository.findByMovieId(movie.getId()));
                            log.info("Found review for movie: {}", movie.getId());
                            break;
                    }
                }
        );
        return map;
    }

    private List<?> getAdditionalInfoByType(EnrichmentType type, Map<EnrichmentType, List<?>> map) {
        return map.get(type).isEmpty() ? List.of() : map.get(type);
    }

    private Movie enrichInfo(Movie movie, List<Genre> genres, List<Country> countries, List<Review> reviews) {
        if (!genres.isEmpty()) movie.setGenres(genres);
        if (!countries.isEmpty()) movie.setCountries(countries);
        if (!reviews.isEmpty()) movie.setReviews(reviews);
        return movie;
    }

}
