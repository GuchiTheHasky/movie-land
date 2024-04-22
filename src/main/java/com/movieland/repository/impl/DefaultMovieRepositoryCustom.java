package com.movieland.repository.impl;

import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.repository.MovieRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultMovieRepositoryCustom implements MovieRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<Movie> findAllCustomSortedMovies(String sortBy, String sortOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaBuilderQuery = criteriaBuilder.createQuery(Movie.class);

        Root<Movie> movieRoot = criteriaBuilderQuery.from(Movie.class);

        if (sortOrder.equals("asc")) {
            criteriaBuilderQuery.orderBy(
                    criteriaBuilder.asc(movieRoot.get(sortBy)));
        } else {
            criteriaBuilderQuery.orderBy(
                    criteriaBuilder.desc(movieRoot.get(sortBy)));
        }

        criteriaBuilderQuery.select(movieRoot);

        TypedQuery<Movie> query = entityManager.createQuery(criteriaBuilderQuery);
        return query.getResultList();
    }


    @Override
    public List<Movie> findAllByGenreIdCustomSortedMovies(int genreId, String sortBy, String sortOrder) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> movie = criteriaQuery.from(Movie.class);
        List<Predicate> predicates = new ArrayList<>();

        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
        Root<Movie> subqueryMovie = subquery.from(Movie.class);
        Join<Genre, Movie> subqueryGenre = subqueryMovie.join("genres");

        subquery.select(subqueryMovie.get("id")).where(
                criteriaBuilder.equal(subqueryGenre.get("id"), genreId));

        predicates.add(criteriaBuilder.in(movie.get("id")).value(subquery));
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        if (sortOrder.equals("asc")) {
            criteriaQuery.orderBy(
                    criteriaBuilder.asc(movie.get(sortBy)));
        } else {
            criteriaQuery.orderBy(
                    criteriaBuilder.desc(movie.get(sortBy)));
        }
        TypedQuery<Movie> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

}
