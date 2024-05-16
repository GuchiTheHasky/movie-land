package com.movieland.repository;

import com.movieland.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    @Query("SELECT g FROM Genre g JOIN g.movies m WHERE m.id = :movieId")
    List<Genre> findByMovieId(int movieId);

}
