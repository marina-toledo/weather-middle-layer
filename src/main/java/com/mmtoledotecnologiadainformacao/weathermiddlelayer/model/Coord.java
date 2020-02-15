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
public class Coord implements Serializable {

    //City geo location, latitude
    Double lat;

    // City geo location, longitude
    Double lon;

}
