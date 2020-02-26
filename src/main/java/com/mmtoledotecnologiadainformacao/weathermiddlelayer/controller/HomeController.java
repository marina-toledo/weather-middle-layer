package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLocationByNextDayTemperature(HttpServletRequest request) {

        logger.info("Requesting Home Page: " + request.getServletPath());

        final String welcomeMessage = "Welcome! Please try one of the following requests:" +
                "\n/weather/locations/1270260?unit=celsius" +
                "\n/weather/summary?unit=celsius&locations=6619279,707860&temperature=5";
        return ResponseEntity.ok(welcomeMessage);
    }

}
