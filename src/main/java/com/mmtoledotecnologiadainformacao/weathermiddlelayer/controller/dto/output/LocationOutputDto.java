package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller.dto.output;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.ApiData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class LocationOutputDto {

    private String locationId;

    public static List<LocationOutputDto> listFromApiDataList(List<ApiData> dataList) {

        return dataList.stream()
                .map(data -> new LocationOutputDto(Long.toString(data.getCity().getId()))).collect(Collectors.toList());
    }

}
