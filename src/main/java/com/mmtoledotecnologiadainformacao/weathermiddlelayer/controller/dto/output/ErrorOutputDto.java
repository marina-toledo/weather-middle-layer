package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorOutputDto {

    private Instant timestamp;

    private Integer status;

    private String error;

    private String message;

    private String trace;

    private String path;

}
