package com.movieland.service;

import com.movieland.dto.ReviewToSaveDto;
import com.movieland.entity.Review;

import java.util.List;

public interface ReviewService {

    void saveReview(ReviewToSaveDto review, String authHeader);

    List<Review> findByMovieId(int movieId);
}
