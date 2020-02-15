package com.mmtoledotecnologiadainformacao.weathermiddlelayer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class City implements Serializable {

    //City ID
    Long id;

    // City name
    String name;

    Coord coord;

    // Country code (GB, JP etc.)
    String country;

    // Shift in seconds from UTC
    Integer timezone;

//    Integer sunrise;

//    Integer sunset;

}
