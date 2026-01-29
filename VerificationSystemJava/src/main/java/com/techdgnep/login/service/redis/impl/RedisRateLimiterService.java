package com.techdgnep.login.service.redis.impl;

import org.springframework.stereotype.Service;

@Service
public class RedisRateLimiterService {
    private CustomRedisMapper<String,String> limiter;

}
