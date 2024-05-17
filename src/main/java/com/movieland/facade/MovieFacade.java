package com.movieland.facade;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;

public interface MovieFacade {
    MovieFullInfoDto findFullMovieInfoById(int movieId, Currency currency);

    MovieFullInfoDto update(int id, MovieAdminDto movieAdminDto);
}
