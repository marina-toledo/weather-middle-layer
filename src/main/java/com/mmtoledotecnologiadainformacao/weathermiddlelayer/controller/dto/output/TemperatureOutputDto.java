package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller.dto.output;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.ApiData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class TemperatureOutputDto {

    private Double temperature;

    public static List<TemperatureOutputDto> listFromApiData(ApiData apiData) {

        return apiData.getList().stream()
                .map(data -> new TemperatureOutputDto(data.getMain().getTemp())).collect(Collectors.toList());
    }

}
