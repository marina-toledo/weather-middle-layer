package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller.dto.output.ErrorOutputDto;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.service.RateLimitException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorOutputDto handleInputFormat(WebRequest request, IllegalArgumentException e) {

        return getErrorOutputDto(request, HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler({RateLimitException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorOutputDto handleExcessiveCalls(WebRequest request, RateLimitException e) {

        return getErrorOutputDto(request, HttpStatus.TOO_MANY_REQUESTS, e);
    }

    private ErrorOutputDto getErrorOutputDto(WebRequest request, HttpStatus status, RuntimeException e) {
        return new ErrorOutputDto(Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                e.getMessage(),
                ExceptionUtils.getStackTrace(e),
                request.getContextPath());
    }

}
