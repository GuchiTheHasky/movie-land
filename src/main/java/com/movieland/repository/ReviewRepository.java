package com.movieland.repository;

import com.movieland.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("select r from Review r where r.movie.id = :movieId")
    List<Review> findByMovieId(int movieId);

}
