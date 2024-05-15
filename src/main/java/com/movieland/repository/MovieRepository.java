package com.movieland.repository;

import com.movieland.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer>, MovieRepositoryCustom {

    @Query(value = "SELECT * FROM movies ORDER BY random() LIMIT 3", nativeQuery = true)
    List<Movie> findThreeRandomMovies();

    @EntityGraph(attributePaths = {"countries", "genres", "reviews"})
    Optional<Movie> findById(int movieId);

}
