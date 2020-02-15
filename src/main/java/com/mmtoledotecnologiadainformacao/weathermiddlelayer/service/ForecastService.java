package com.mmtoledotecnologiadainformacao.weathermiddlelayer.service;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.WeatherApiData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
public class ForecastService {

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    private RestTemplate restTemplate;

    @Autowired
    public ForecastService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public WeatherApiData getForecastForNextDays(Long cityId) {

        String url = MessageFormat.format(weatherApiUrl, Long.toString(cityId), weatherApiKey);
        ResponseEntity<WeatherApiData> weatherApiData = restTemplate.getForEntity(url, WeatherApiData.class);
        return weatherApiData.getBody();
        //todo handle different responses from Third API, specially errors
    }

}
