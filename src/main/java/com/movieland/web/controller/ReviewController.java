package com.movieland.web.controller;

import com.movieland.dto.ReviewToSaveDto;
import com.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    public void addReview(@RequestBody ReviewToSaveDto review, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Posting review");
        reviewService.saveReview(review, authHeader);
    }
}









