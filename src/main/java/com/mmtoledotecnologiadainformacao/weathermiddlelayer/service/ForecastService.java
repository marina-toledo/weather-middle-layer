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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mmtoledotecnologiadainformacao.weathermiddlelayer.configuration.CacheConfig.CALL_PER_MINUTE;

@Service
public class ForecastService {

    private static final Logger logger = LoggerFactory.getLogger(ForecastService.class);

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
    public ForecastService(RestTemplate restTemplate, RedisCacheManager redisCacheManager) {
        this.restTemplate = restTemplate;
        this.redisCacheManager = redisCacheManager;
    }


    //    "Algorithm Pattern: Rate limiter 2" shown in https://redis.io/commands/INCR
    public synchronized void checkRateLimit() {

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

//        Integer callPerMinute = GET(CALL_PER_MINUTE);
//        if (callPerMinute != null && callPerMinute >= 60) {
//            throw new RateLimitException();
//        }
//
//        callPerMinute=INCR(callPerMinute);
//        if(callPerMinute==1){
//            EXPIRE(callPerMinute, 1minuto);
//        }
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

    //    todo cache does not work, because method below its on the same class and AOP does not allow it
    public List<ApiData> getLocationByNextDayTemperature(String temperatureUnit, String concatenatedLocationIds, Integer temperature) {

        String[] locationId = concatenatedLocationIds.split(",");

        return Arrays.stream(locationId)
                .map(location -> requestWeatherDataTo3rdAPI(Long.valueOf(location), temperatureUnit))
                .filter(data ->
                        data.getList().stream().anyMatch(list -> LocalDate.now().equals(list.getDt_txt().toLocalDate().minusDays(1))//tomorrow
                                && list.getMain().getTemp() > temperature))
                .collect(Collectors.toList());
    }

}
