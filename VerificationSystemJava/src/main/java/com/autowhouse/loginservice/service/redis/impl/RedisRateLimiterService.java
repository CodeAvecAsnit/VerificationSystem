package com.autowhouse.loginservice.service.redis.impl;

import com.autowhouse.loginservice.service.redis.RateLimiterService;
import com.autowhouse.loginservice.util.CustomRedisMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisRateLimiterService implements RateLimiterService {

    private static final Duration EXPIRY_DURATION = Duration.ofMinutes(5);
    private static final String KEY_PREFIX = "password:attempt:";
    private final CustomRedisMapper<String,Integer> rateCache;


    public RedisRateLimiterService(@Qualifier("rateCache") CustomRedisMapper<String, Integer> rateCache) {
        this.rateCache = rateCache;
    }


    private String getKey(String email) {
        return KEY_PREFIX + email.toLowerCase();
    }

    @Override
    public Optional<Integer> getTries(String email) {
        String key = getKey(email);
        return rateCache.getAndRefreshOptional(key, EXPIRY_DURATION);
    }

    @Override
    public void setTries(String email, int tryNumber) {
        String key = getKey(email);
        rateCache.set(key, tryNumber, EXPIRY_DURATION);
    }


    public int incrementTries(String email) {
        String key = getKey(email);
        Long count = rateCache.increment(key);
        rateCache.setExpire(key, EXPIRY_DURATION);
        return count != null ? count.intValue() : 1;
    }

    @Override
    public void onSuccessRemove(String email, int tryNumber) {
        if (tryNumber != 1) {
            String key = getKey(email);
            rateCache.remove(key);
        }
    }

    public void clearTries(String email) {
        String key = getKey(email);
        rateCache.remove(key);
    }

    public boolean isLockedOut(String email, int maxAttempts) {
        return getTries(email).map(tries -> tries >= maxAttempts).orElse(false);
    }
}
