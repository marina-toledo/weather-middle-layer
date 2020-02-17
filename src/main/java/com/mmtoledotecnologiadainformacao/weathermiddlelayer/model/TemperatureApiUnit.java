package com.mmtoledotecnologiadainformacao.weathermiddlelayer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemperatureApiUnit {

    CELSIUS("metric"),
    FAREHEINT("imperial");

    private String apiUnit;

}
