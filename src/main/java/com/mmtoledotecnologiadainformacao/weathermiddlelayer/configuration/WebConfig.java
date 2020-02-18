package com.mmtoledotecnologiadainformacao.weathermiddlelayer.configuration;

import com.mmtoledotecnologiadainformacao.weathermiddlelayer.model.TemperatureApiUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverter());
    }

    public class StringToEnumConverter implements Converter<String, TemperatureApiUnit> {
        @Override
        public TemperatureApiUnit convert(String source) {
            return TemperatureApiUnit.valueOf(source.toUpperCase());
        }
    }

}
