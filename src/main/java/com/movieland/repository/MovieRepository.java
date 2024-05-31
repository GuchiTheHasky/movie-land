package com.movieland.repository;

import com.movieland.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer>, MovieRepositoryCustom {

    @Query(value = "SELECT * FROM movies ORDER BY random() LIMIT 3", nativeQuery = true)
    List<Movie> findThreeRandomMovies();

    //@EntityGraph(attributePaths = {"countries", "genres", "reviews"})
    @Query("SELECT m FROM Movie m WHERE m.id = :movieId")
    Optional<Movie> findById(@Param("movieId") int movieId);

}
