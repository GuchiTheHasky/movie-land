package com.movieland.service;

import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.entity.Review;

import java.util.List;
import java.util.concurrent.Future;

public interface EnrichmentService {

    void enrichAdditionalInfo(int movieId, Movie movie);

}
