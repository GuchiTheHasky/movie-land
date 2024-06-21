package com.movieland.exception;


public class EnrichmentException extends RuntimeException {

    private static final String MESSAGE = "%s : %s";

    public EnrichmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnrichmentException(String message, String type) {
        super(String.format(MESSAGE, message, type));
    }
}

