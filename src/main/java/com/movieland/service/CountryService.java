package com.movieland.service;

import com.movieland.entity.Country;

import java.util.List;


public interface CountryService {

    List<Country> findAllCountriesById(List<Integer> countryIds);

    List<Country> findByMovieId(int movieId);
}
