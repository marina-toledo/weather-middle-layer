package com.mmtoledotecnologiadainformacao.weathermiddlelayer.service;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.ApiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForecastService {

    private static final Logger logger = LoggerFactory.getLogger(ForecastService.class);

    private WeatherApiCaller weatherApiCaller;


    @Autowired
    public ForecastService(WeatherApiCaller weatherApiCaller) {
        this.weatherApiCaller = weatherApiCaller;
    }


    public List<ApiData> getLocationByNextDayTemperature(String temperatureUnit, String concatenatedLocationIds, Integer temperature) {

        String[] locationId = concatenatedLocationIds.split(",");

        return Arrays.stream(locationId)
                .map(location -> weatherApiCaller.requestWeatherDataTo3rdAPI(Long.valueOf(location), temperatureUnit))
                .filter(data ->
                        data.getList().stream().anyMatch(list -> LocalDate.now().equals(list.getDt_txt().toLocalDate().minusDays(1))//tomorrow
                                && list.getMain().getTemp() > temperature))
                .collect(Collectors.toList());
    }

}
