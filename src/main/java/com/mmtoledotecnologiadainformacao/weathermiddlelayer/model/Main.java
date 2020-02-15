package com.mmtoledotecnologiadainformacao.weathermiddlelayer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class Main {

    //Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    Double temp;

    // Temperature. This temperature parameter accounts for the human perception of weather. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    Double feels_like;

    // Minimum temperature at the moment of calculation. This is deviation from 'temp' that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    Double temp_min;

    // Maximum temperature at the moment of calculation. This is deviation from 'temp' that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    Double temp_max;

    // Atmospheric pressure on the sea level by default, hPa
    Double pressure;

    // Atmospheric pressure on the sea level, hPa
    Double sea_level;

    // Atmospheric pressure on the ground level, hPa
    Double grnd_level;

    // Humidity, %
    Double humidity;

    // Internal parameter
    Double temp_kf;

}
