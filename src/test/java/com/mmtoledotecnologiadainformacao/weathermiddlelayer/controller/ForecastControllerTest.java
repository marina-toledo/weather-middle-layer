package com.mmtoledotecnologiadainformacao.weathermiddlelayer.controller;

import org.junit.jupiter.api.AfterEach;
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
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private static final String USERS_LOCATIONS_ENDPOINT = "/weather/summary?unit=celsius&temperature=5&locations={0},{1}";
    private static final String USERS_LOCATIONS_ENDPOINT_EXCEED_CALLS = "/weather/summary?unit=celsius&temperature=5&locations={0},{1},{2}";
    private static final List<String> cityIds = Arrays.asList("707860", "519188", "1283378", "1270260", "708546");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LettuceConnectionFactory redisConnectionFactory;


    @BeforeEach
    public void beforeEachTest() {
        cleanRedisCache();
    }

    @AfterEach
    public void afterAll() {
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

    @Test
    public void shouldReturnUsersLocationWithMinTemperatureTomorrow() throws Exception {

        String url = MessageFormat.format(USERS_LOCATIONS_ENDPOINT, cityIds.get(0), cityIds.get(1));
        URI uri = new URI(url);

        MockHttpServletRequestBuilder request = get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void shouldExceedRateLimitForMinuteCalls() throws Exception {

        String url = MessageFormat.format(USERS_LOCATIONS_ENDPOINT_EXCEED_CALLS, cityIds.get(0), cityIds.get(1), cityIds.get(2), cityIds.get(3), cityIds.get(4));
        URI uri = new URI(url);

        MockHttpServletRequestBuilder request = get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void shouldExceedRateLimitForDailyCalls() throws Exception {//todo!!

        String url = MessageFormat.format(USERS_LOCATIONS_ENDPOINT, cityIds.get(0), cityIds.get(1));
        URI uri2calls = new URI(url);

        MockHttpServletRequestBuilder request2calls = get(uri2calls)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request2calls)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(notNullValue()));

        TimeUnit.MINUTES.sleep(1);

        String url2 = MessageFormat.format(USERS_LOCATIONS_ENDPOINT, cityIds.get(2), cityIds.get(3));
        URI uri = new URI(url2);

        MockHttpServletRequestBuilder request = get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void shouldReturnErrorGivenWrongTemperatureUnitInput() throws Exception {
        URI uri = new UriTemplate("/weather/locations/{cityId}?unit=WRONG").expand(cityIds.get(0));

        MockHttpServletRequestBuilder request = get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void shouldReturnErrorGivenWrongLocationsInput() throws Exception {
        URI uri = new UriTemplate("/weather/summary?unit=celsius&temperature=5&locations=WRONG1,WRONG2").expand(cityIds.get(0));

        MockHttpServletRequestBuilder request = get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(notNullValue()));
    }

}
