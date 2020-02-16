package com.mmtoledotecnologiadainformacao.weathermiddlelayer.service;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.WeatherApiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
public class ForecastService {

    private static final Logger logger = LoggerFactory.getLogger(ForecastService.class);

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    private RestTemplate restTemplate;

    @Autowired
    public ForecastService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "forecast")
    public WeatherApiData getForecastForNextDays(Long cityId) {

        logger.info("Forwarding request to list the forecast for the next days to Open Weather API. Location ID: " + cityId);

        String url = MessageFormat.format(weatherApiUrl, Long.toString(cityId), weatherApiKey);
        ResponseEntity<WeatherApiData> weatherApiData = restTemplate.getForEntity(url, WeatherApiData.class);
        return weatherApiData.getBody();
        //todo handle different responses from Third API, specially errors
    }

}
