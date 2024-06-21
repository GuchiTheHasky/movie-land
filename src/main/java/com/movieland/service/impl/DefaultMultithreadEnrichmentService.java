package com.movieland.service.impl;

import com.movieland.entity.*;
import com.movieland.exception.EnrichmentException;
import com.movieland.repository.CountryRepository;
import com.movieland.repository.GenreRepository;
import com.movieland.repository.ReviewRepository;
import com.movieland.service.EnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;


@Slf4j
@Service
@Profile("multi")
@RequiredArgsConstructor
public class DefaultMultithreadEnrichmentService implements EnrichmentService {

    private static final Integer TIME_OUT = 5;

    private final ExecutorService executorService;

    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Movie enrichAdditionalInfo(Movie movie, EnrichmentType... enrichmentTypes) {

        try {
            List<Callable<Object>> tasksList = getCallableTasksList(movie, enrichmentTypes);
            List<Callable<Object>> tasks = tasksList.stream().filter(Objects::nonNull).toList();
            List<Future<Object>> invokedObjects = executorService.invokeAll(tasks, TIME_OUT, TimeUnit.SECONDS);
            log.info("Enrichment tasks invoked successfully");

            Map<EnrichmentType, Object> enrichmentTypeObjectMap = fetchEnrichmentTypeObjectMap(enrichmentTypes, invokedObjects);

            List<Genre> genres = getAdditionalInfoByType(EnrichmentType.GENRE, enrichmentTypeObjectMap);
            List<Country> countries = getAdditionalInfoByType(EnrichmentType.COUNTRY, enrichmentTypeObjectMap);
            List<Review> reviews = getAdditionalInfoByType(EnrichmentType.REVIEW, enrichmentTypeObjectMap);

            return enrichInfo(movie, genres, countries, reviews);
        } catch (InterruptedException e) {
            log.error("Error fetching data: ", e);
            Thread.currentThread().interrupt();
        }
        return movie;
    }

    private List<Callable<Object>> getCallableTasksList(Movie movie, EnrichmentType[] enrichmentTypes) {
        List<Callable<Object>> tasks = new ArrayList<>();

        Arrays.stream(enrichmentTypes).forEach(type -> {
                    switch (type) {
                        case GENRE:
                            tasks.add(() -> {
                                List<Genre> genres = genreRepository.findByMovieId(movie.getId());
                                log.info("Data fetched successfully for type: GENRE");
                                return genres;
                            });
                            break;
                        case COUNTRY:
                            tasks.add(() -> {
                                List<Country> countries = countryRepository.findByMovieId(movie.getId());
                                log.info("Data fetched successfully for type: COUNTRY");
                                return countries;
                            });
                            break;
                        case REVIEW:
                            tasks.add(() -> {
                                List<Review> reviews = reviewRepository.findByMovieId(movie.getId());

                                log.info("Data fetched successfully for type: REVIEW");
                                return reviews;
                            });
                    }
                }
        );
        return tasks;
    }

    private Map<EnrichmentType, Object> fetchEnrichmentTypeObjectMap(EnrichmentType[] enrichmentTypes, List<Future<Object>> invokedObjects) throws InterruptedException {
        Map<EnrichmentType, Object> enrichmentTypeObjectMap = new EnumMap<>(EnrichmentType.class);

        for (int i = 0; i < enrichmentTypes.length; i++) {
            Future<Object> futures = invokedObjects.get(i);
            EnrichmentType type = enrichmentTypes[i];

            try {
                Object object = futures.get();
                enrichmentTypeObjectMap.put(type, object);
                log.info("Data fetched successfully for type: {}", type);
            } catch (CancellationException e) {
                log.error("Task was cancelled for type: {}", type, e);
                throw new EnrichmentException("Task was cancelled for type: " + type, e);
            } catch (ExecutionException e) {
                log.error("Error fetching data for type: {}", type, e);
                throw new EnrichmentException("Error fetching data for type: " + type, e);
            }
        }
        return enrichmentTypeObjectMap;
    }

    private <T> List<T> getAdditionalInfoByType(EnrichmentType type, Map<EnrichmentType, Object> enrichmentTypeObjectMap) {
        return enrichmentTypeObjectMap.containsKey(type) ? (List<T>) enrichmentTypeObjectMap.get(type) : List.of();
    }

    private Movie enrichInfo(Movie movie, List<Genre> genres, List<Country> countries, List<Review> reviews) {
        if (genres != null && !genres.isEmpty())
            movie.setGenres(genres);
        if (countries != null && !countries.isEmpty())
            movie.setCountries(countries);
        if (reviews != null && !reviews.isEmpty())
            movie.setReviews(reviews);
        return movie;
    }
}