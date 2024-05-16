package com.movieland.exception;

public class MovieNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Сan't find movie with id: %s";

    public MovieNotFoundException(int id) {
        super(String.format(MESSAGE, id));
    }
}
