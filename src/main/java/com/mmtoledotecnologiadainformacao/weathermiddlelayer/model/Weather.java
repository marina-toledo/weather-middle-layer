package com.mmtoledotecnologiadainformacao.weathermiddlelayer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class Weather {

    // Weather condition id
    Long id;

    // Group of weather parameters (Rain, Snow, Extreme etc.)
    String main;

    // Weather condition within the group. You can get the output in your language.
    String description;

    // Weather icon id
    String icon;

}
