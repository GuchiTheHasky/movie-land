package com.movieland.dto;

import com.movieland.entity.Country;
import com.movieland.entity.Genre;
import lombok.Setter;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Setter
@Getter
@Builder
public class MovieFullInfoDto {

    private int id;

    private String nameRussian;

    private String nameNative;

    private int yearOfRelease;

    private String description;

    private Double rating;

    private Double price;

    private String picturePath;

    private List<Country> countries;

    private List<Genre> genres;

    private List<ReviewDto> reviews;
}