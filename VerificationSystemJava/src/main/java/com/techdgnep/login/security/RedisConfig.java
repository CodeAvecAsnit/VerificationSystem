package com.techdgnep.login.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techdgnep.login.data.dto.DetailsCodeDTO;
import com.techdgnep.login.util.CustomRedisMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean(name = "detailsCache")
    public CustomRedisMapper<String, DetailsCodeDTO> customRedisMapper(RedisConnectionFactory redisConnectionFactory) {
        return createRedisMapper(redisConnectionFactory, DetailsCodeDTO.class);
    }

    @Bean(name = "rateCache")
    public CustomRedisMapper<String, Integer> customRedisMapperPassword(RedisConnectionFactory redisConnectionFactory) {
        return createRedisMapper(redisConnectionFactory, Integer.class);
    }

    private <V> CustomRedisMapper<String, V> createRedisMapper(
            RedisConnectionFactory redisConnectionFactory,
            Class<V> valueType) {

        CustomRedisMapper<String, V> template = new CustomRedisMapper<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        Jackson2JsonRedisSerializer<V> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, valueType);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}