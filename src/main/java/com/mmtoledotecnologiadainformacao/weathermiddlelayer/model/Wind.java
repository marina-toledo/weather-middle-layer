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
public class Wind implements Serializable {

    // Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    Double speed;

    // Wind direction, degrees (meteorological)
    Double deg;

}
