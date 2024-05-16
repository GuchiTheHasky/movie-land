package com.movieland.repository;

import com.movieland.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query("SELECT c FROM Country c JOIN c.movies m WHERE m.id = :movieId")
    List<Country> findByMovieId(int movieId);

}
