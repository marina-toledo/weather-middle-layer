package com.mmtoledotecnologiadainformacao.weathermiddlelayer.service;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.ApiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public ApiData requestWeatherDataFrom3rdAPI(Long cityId) {

        logger.info("Forwarding request to list the forecast for the next days to Open Weather API. Location ID: " + cityId);

        String url = MessageFormat.format(weatherApiUrl, Long.toString(cityId), weatherApiKey);
        ResponseEntity<ApiData> weatherApiData = restTemplate.getForEntity(url, ApiData.class);
        return weatherApiData.getBody();
        //todo handle different responses from Third API, specially errors
    }

    //    todo unit temperature
    //    For temperature in Fahrenheit use units = imperial
    //    For temperature in Celsius use units = metric
    //    todo cache does not work, because method below its on the same class and AOP does not allow it
    public List<ApiData> getLocationByNextDayTemperature(String temperatureUnit, String concatenatedLocationIds, Integer temperature) {

        List<ApiData> apiDataList = new ArrayList<>();

        Arrays.stream(concatenatedLocationIds.split(","))
                .forEachOrdered(location -> apiDataList.add(requestWeatherDataFrom3rdAPI(Long.valueOf(location))));

        return apiDataList.stream()
                .filter(data -> data.getList().stream().anyMatch(list -> LocalDate.now().equals(list.getDt_txt().toLocalDate().minusDays(1))
                        && list.getMain().getTemp() > temperature))
                .collect(Collectors.toList());
    }

}
