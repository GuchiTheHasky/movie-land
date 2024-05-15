package com.movieland.mapper;

import com.movieland.dto.ReviewDto;
import com.movieland.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ReviewMapper {

    ReviewDto toReviewDto(Review review);

    List<ReviewDto> toReviewDto(List<Review> reviews);


}
