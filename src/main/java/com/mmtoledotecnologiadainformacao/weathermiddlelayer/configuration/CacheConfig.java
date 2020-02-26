package com.mmtoledotecnologiadainformacao.weathermiddlelayer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;

/**
 * @link https://docs.spring.io/spring-data/redis/docs/2.2.4.RELEASE/reference/html/#redis:support:cache-abstraction
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    public static final String CACHE_MINUTE = "CACHE_MINUTE";

    public static final String CACHE_DAY = "CACHE_DAY";

    @Value("${REDIS_URL:redis://localhost:6379}")
    private String redisUrl;

    @Value("${weather.cache.ttl.minute}")
    private Integer cacheTTL;


    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {//todo minor urgency: improve cache keys
        return (target, method, params) ->
                target.getClass().getSimpleName() + "_"
                        + method.getName() + "_"
                        + StringUtils.arrayToDelimitedString(params, "_");
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() throws URISyntaxException {

        final URI redisUri = new URI(redisUrl);
        final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisUri.getHost(), redisUri.getPort());

        if (redisUrl.contains("@")) {//has password
            final String authority = redisUri.getAuthority();
            final int start = authority.indexOf(':');
            final int end = authority.indexOf('@');
            configuration.setPassword(authority.substring(start + 1, end));
        }

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheTTL));

        RedisCacheConfiguration minuteConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1));

        RedisCacheConfiguration dayConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(Map.of(CACHE_MINUTE, minuteConfig, CACHE_DAY, dayConfig))
                .transactionAware()
                .build();
    }

}
