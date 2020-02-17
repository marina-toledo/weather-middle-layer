package com.mmtoledotecnologiadainformacao.weathermiddlelayer.service;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.ApiData;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.TemperatureApiUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

import static com.mmtoledotecnologiadainformacao.weathermiddlelayer.configuration.CacheConfig.CALL_PER_MINUTE;

@Component
public class WeatherApiCaller {

    private static final Logger logger = LoggerFactory.getLogger(WeatherApiCaller.class);

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.max-calls.day}")
    private Integer maxCallPerDay;

    @Value("${weather.api.max-calls.minute}")
    private Integer maxCallPerMinute;

    private RestTemplate restTemplate;

    private RedisCacheManager redisCacheManager;


    @Autowired
    public WeatherApiCaller(RestTemplate restTemplate, RedisCacheManager redisCacheManager) {
        this.restTemplate = restTemplate;
        this.redisCacheManager = redisCacheManager;
    }


    //    "Algorithm Pattern: Rate limiter 2" shown in https://redis.io/commands/INCR
    public synchronized void checkRateLimit() {
//todo add cache per day
        Cache cache = redisCacheManager.getCache(CALL_PER_MINUTE);
        Integer value = cache.get("count", Integer.class);

        if (value != null && value >= maxCallPerMinute) {
            throw new RuntimeException("RATE LIMIT CALL EXCEPTION");
        }

        if (value == null) {
            cache.put("count", 1);
        } else {
            cache.put("count", value + 1);
        }
    }


    @Cacheable(value = "forecast")
    public ApiData requestWeatherDataTo3rdAPI(Long cityId, String unit) {

        logger.info("Forwarding request to list the forecast for the next days to Open Weather API. Location ID: " + cityId);

        checkRateLimit();

        String apiUnit = TemperatureApiUnit.valueOf(unit.toUpperCase()).getApiUnit();

        String url = MessageFormat.format(weatherApiUrl, Long.toString(cityId), weatherApiKey, apiUnit);
        ResponseEntity<ApiData> weatherApiData = restTemplate.getForEntity(url, ApiData.class);
        return weatherApiData.getBody();
        //todo handle different responses from Third API, specially errors
    }

}
