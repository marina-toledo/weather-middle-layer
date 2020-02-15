package com.mmtoledotecnologiadainformacao.weathermiddlelayer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class WeatherList {

    // Time of data forecasted, unix, UTC
    Long dt;

    Main main;

    // more info Weather condition codes
    List<Weather> weather;

    Clouds clouds;

    Wind wind;

    Rain rain;

    Snow snow;

    // Time of data forecasted, ISO, UTC
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dt_txt;

}
