package com.movieland.service;

import com.movieland.entity.*;

public interface EnrichmentService {

    Movie enrichAdditionalInfo(Movie movie, EnrichmentType... enrichmentTypes);

}
