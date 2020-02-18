package com.mmtoledotecnologiadainformacao.weathermiddlelayer.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // following lines were added because of a self reference cycle bug
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(org.springframework.core.ResolvableType.class, MyMixInForIgnoreType.class);
        return mapper;
    }

    @JsonIgnoreType
    public class MyMixInForIgnoreType {
    }

}
