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
import java.util.Map;

import static com.mmtoledotecnologiadainformacao.weathermiddlelayer.configuration.CacheConfig.CACHE_DAY;
import static com.mmtoledotecnologiadainformacao.weathermiddlelayer.configuration.CacheConfig.CACHE_MINUTE;

@Component
public class WeatherApiCaller {

    private static final Logger logger = LoggerFactory.getLogger(WeatherApiCaller.class);

    private static final String COUNTER = "COUNTER";

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    private RestTemplate restTemplate;

    private RedisCacheManager redisCacheManager;

    private final Map<String, Integer> cacheMaxCall;


    @Autowired
    public WeatherApiCaller(RestTemplate restTemplate, RedisCacheManager redisCacheManager,
                            @Value("${weather.api.max-calls.minute}") Integer maxCallPerMinute,
                            @Value("${weather.api.max-calls.day}") Integer maxCallPerDay) {
        this.restTemplate = restTemplate;
        this.redisCacheManager = redisCacheManager;
        cacheMaxCall = Map.of(CACHE_MINUTE, maxCallPerMinute, CACHE_DAY, maxCallPerDay);
    }

    //    "Algorithm Pattern: Rate limiter 2" shown in https://redis.io/commands/INCR
    public synchronized void checkRateLimit() {

        cacheMaxCall.forEach((cacheName, maxCall) -> {
            Cache cache = redisCacheManager.getCache(cacheName);
            Integer counter = cache.get(COUNTER, Integer.class);

            if (counter != null && counter >= maxCall) {
                logger.info(" App exceeded calls in " + cacheName + ". Max calls permitted is " + maxCall + ".");
                throw new RateLimitException("Exceeded calls in " + cacheName + ". Max calls permitted is " + maxCall + ".");
            }

            if (counter == null) {
                cache.put(COUNTER, 1);
            } else {
                cache.put(COUNTER, counter + 1);
            }
        });
    }

    @Cacheable(value = "forecast")
    public ApiData requestWeatherDataTo3rdAPI(Long cityId, TemperatureApiUnit unit) {

        logger.info("Forwarding request to list the forecast for the next days to Open Weather API. Location ID: " + cityId);

        checkRateLimit();

        String url = MessageFormat.format(weatherApiUrl, Long.toString(cityId), weatherApiKey, unit.getApiUnit());
        ResponseEntity<ApiData> weatherApiData = restTemplate.getForEntity(url, ApiData.class);
        return weatherApiData.getBody();
    }

}
