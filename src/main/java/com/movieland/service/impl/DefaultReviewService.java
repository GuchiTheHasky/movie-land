package com.movieland.service.impl;

import com.movieland.dto.ReviewToSaveDto;
import com.movieland.entity.Movie;
import com.movieland.entity.Review;
import com.movieland.entity.User;
import com.movieland.repository.ReviewRepository;
import com.movieland.service.MovieService;
import com.movieland.service.ReviewService;
import com.movieland.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.movieland.service.impl.AuthenticationService.BEARER;

@Service
@AllArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final MovieService movieService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    @Override
    public void saveReview(ReviewToSaveDto reviewToSaveDto, String authHeader) {
        String token = authHeader.replaceFirst(BEARER, StringUtils.EMPTY);
        User user = userService.findByEmail(jwtService.extractUsername(token));
        Movie movie = movieService.findMovieByReferenceId(reviewToSaveDto.getMovieId());

        Review review = Review.builder()
                .movie(movie)
                .user(user)
                .text(reviewToSaveDto.getText())
                .build();

        reviewRepository.save(review);
    }


}
