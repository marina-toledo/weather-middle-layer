package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ForecastControllerTest {

    private static final String FORECAST_ENDPOINT = "/weather/locations/{cityId}?unit=celsius";
    private static final String USERS_LOCATIONS_ENDPOINT = "/weather/summary?unit=celsius&temperature=5&locations=6619279,707860";
    private static final List<String> cityIds = Arrays.asList("707860", "519188", "1283378", "1270260", "708546");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LettuceConnectionFactory redisConnectionFactory;

    @BeforeEach
    public void beforeEachTest() {
        cleanRedisCache();
    }

    private void cleanRedisCache() {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.flushAll();
        redisConnection.close();
    }

    //todo mock third API call and assert content of response

    @Test
    public void shouldReturnForecastForNextFiveDays() throws Exception {
        URI uri = new UriTemplate(FORECAST_ENDPOINT).expand(cityIds.get(0));

        MockHttpServletRequestBuilder request = get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(notNullValue()));
    }

//todo
//    shouldReturnUsersLocationWithMinTemperatureTomorrow
//    shouldExceedRateLimitForMinuteCalls
//    shouldExceedRateLimitForDailyCalls
//    shouldReturnErrorGivenWrongTemperatureUnitInput
//    shouldReturnErrorGivenWrongLocationsInput

}
