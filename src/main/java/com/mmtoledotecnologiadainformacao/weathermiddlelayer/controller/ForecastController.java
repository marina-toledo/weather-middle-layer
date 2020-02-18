package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller.dto.output.LocationOutputDto;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller.dto.output.TemperatureOutputDto;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.ApiData;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.TemperatureApiUnit;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.service.ForecastService;
import com.mmtoledotecnologiadainformacao.weathermiddlelayer.service.WeatherApiCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class ForecastController {

    private static final Logger logger = LoggerFactory.getLogger(ForecastController.class);

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private WeatherApiCaller weatherApiCaller;


    /**
     * @param request         injected by spring
     * @param temperatureUnit <celsius|fahrenheit>
     * @param locations       comma separated list of location ids
     * @param temperature     value of temperature
     * @return A list of the user’s favourite locations where the temperature will be above a certain temperature the next day.
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LocationOutputDto>> getLocationByNextDayTemperature(HttpServletRequest request,
                                                                                   @RequestParam("unit") TemperatureApiUnit temperatureUnit,
                                                                                   @RequestParam("locations") String locations,
                                                                                   @RequestParam("temperature") Integer temperature) {

        logger.info("Request: " + request.getServletPath() + "?" + request.getQueryString());

        final List<ApiData> apiDataList = this.forecastService.getLocationByNextDayTemperature(temperatureUnit, locations, temperature);
        return ResponseEntity.ok(LocationOutputDto.listFromApiDataList(apiDataList));
    }


    /**
     * @param request    injected by spring
     * @param locationId id according to json list
     * @return A list of temperatures for the next 5 days in one specific location.
     */
    @GetMapping(value = "/locations/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TemperatureOutputDto>> getTemperaturesForTheNextFiveDays(HttpServletRequest request,
                                                                                        @PathVariable("locationId") Long locationId,
                                                                                        @RequestParam(defaultValue = "CELSIUS") TemperatureApiUnit unit) {

        logger.info("Request: " + request.getServletPath() + "?" + request.getQueryString());

        final ApiData apiData = this.weatherApiCaller.requestWeatherDataTo3rdAPI(locationId, unit);
        return ResponseEntity.ok(TemperatureOutputDto.listFromApiData(apiData));
    }

}
