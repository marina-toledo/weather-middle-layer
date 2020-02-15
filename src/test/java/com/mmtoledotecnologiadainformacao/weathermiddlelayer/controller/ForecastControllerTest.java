package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ForecastControllerTest {

    //   http://localhost:8080/weather/forecast?day=5&location=6619279
    private static final String ENDPOINT = "/weather/forecast?day=5&location={cityId}";
    private static final String cityId = "6619279";

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void shouldReturnForecastForNextDays() throws Exception {
        URI uri = new UriTemplate(ENDPOINT).expand(cityId);

        MockHttpServletRequestBuilder request = get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(notNullValue()));
        //todo mock third API call and assert content of response
    }

}
