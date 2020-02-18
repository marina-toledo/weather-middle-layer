package com.mmtoledotecnologiadainformacao.weathermiddlelayer.service;

public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }

}
