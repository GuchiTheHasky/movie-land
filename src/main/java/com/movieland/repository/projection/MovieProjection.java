package com.movieland.repository.projection;

public interface MovieProjection {
    int getId();

    String getNameRussian();

    String getNameNative();

    int getYearOfRelease();

    Double getRating();

    Double getPrice();

    String getPicturePath();

    String getDescription();
}
