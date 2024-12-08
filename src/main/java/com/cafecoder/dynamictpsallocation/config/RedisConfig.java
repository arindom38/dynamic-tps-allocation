package com.cafecoder.dynamictpsallocation.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisMasterHost;

    @Value("${spring.data.redis.port}")
    private int redisMasterPort;
    private final RedisSerializer<String> stringSerializer = RedisSerializer.string();

    private final Duration ttl = Duration.ofHours(6);

    @Bean
    public RedisTemplate<String, Object> redisTemplateSerialize(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }

    @Primary
    @Bean
    public CacheManager cacheManager() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisMasterHost, redisMasterPort);

        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();

        RedisCacheWriter writer = RedisCacheWriter.nonLockingRedisCacheWriter(factory);

        return new RedisCacheManager(writer, redisCacheConfiguration());
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .entryTtl(ttl);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisMasterHost, redisMasterPort);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public CacheManager cacheManagerForJson(ObjectMapper objectMapper) {
        LettuceConnectionFactory factory = redisConnectionFactory();
        factory.afterPropertiesSet();

        RedisCacheWriter writer = RedisCacheWriter.nonLockingRedisCacheWriter(factory);

        return RedisCacheManager.builder(writer)
                .cacheDefaults(redisCacheConfigurationForJson(objectMapper))
                .build();
    }


    @Bean
    public RedisCacheConfiguration redisCacheConfigurationForJson(ObjectMapper objectMapper) {
        objectMapper = objectMapper.copy();
        objectMapper = objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                .entryTtl(ttl);
    }
}

