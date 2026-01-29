package com.techdgnep.login.security;


import com.techdgnep.login.data.dto.DetailsCodeDTO;
import com.techdgnep.login.service.redis.impl.CustomRedisMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean(name="detailsCache")
    public CustomRedisMapper<String, DetailsCodeDTO> customRedisMapper(RedisConnectionFactory redisConnectionFactory){
        CustomRedisMapper<String,DetailsCodeDTO> template = new CustomRedisMapper<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.afterPropertiesSet();
        return template;
    }
}