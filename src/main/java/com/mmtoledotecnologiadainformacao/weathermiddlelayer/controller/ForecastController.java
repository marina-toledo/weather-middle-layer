package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.WeatherApiData;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.service.ForecastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/weather")
public class ForecastController {

    private static final Logger logger = LoggerFactory.getLogger(ForecastController.class);

    @Autowired
    private ForecastService forecastService;

    //todo refactor this endpoint, should only return the temperatures and not the whole weather information
    @GetMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherApiData> getForecastForNextDays(HttpServletRequest request,
                                                                 @RequestParam(defaultValue = "5") Long day,
                                                                 @RequestParam("location") Long locationId) {

        logger.info("Request: " + request.getServletPath() + "?" + request.getQueryString());

        final WeatherApiData weatherApiData = this.forecastService.getForecastForNextDays(locationId);
        return ResponseEntity.ok(weatherApiData);
    }

}