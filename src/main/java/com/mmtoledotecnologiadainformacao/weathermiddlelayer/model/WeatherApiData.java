package com.mmtoledotecnologiadainformacao.weathermiddlelayer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class WeatherApiData {

    // Internal parameter
    String cod;

    // Internal parameter
    Integer message;

    // Number of lines returned by this API call
    Integer cnt;

    City city;

    List<WeatherList> list;

}
