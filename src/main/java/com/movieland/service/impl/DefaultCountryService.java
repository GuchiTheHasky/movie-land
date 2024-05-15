package com.movieland.service.impl;

import com.movieland.entity.Country;
import com.movieland.service.CountryRepository;
import com.movieland.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultCountryService implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public List<Country> findAllCountriesById(List<Integer> countryIds) {
        return countryRepository.findAllById(countryIds);
    }
}
