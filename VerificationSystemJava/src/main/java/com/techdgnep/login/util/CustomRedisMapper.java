package com.techdgnep.login.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Asnit Bakhati
 *
 *
 * Generic Redis operations wrapper providing simplified access to common Redis operations.
 * Extends RedisTemplate to provide type safe and convenient methods.
 * @param <K> Key type
 * @param <V> Value type
 */
public class CustomRedisMapper<K, V> extends RedisTemplate<K, V> {

    @Nullable
    public V get(@NonNull K key) {
        return super.opsForValue().get(key);
    }

    /**
     * Get value by key, returning Optional.
     *
     * @param key the key
     * @return Optional containing value if present
     */
    @NonNull
    public Optional<V> getOptional(@NonNull K key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Set key-value pair without expiration.
     */
    public void set(@NonNull K key, @NonNull V value) {
        super.opsForValue().set(key, value);
    }

    /**
     * Set key-value pair with expiration.
     */
    public void set(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit) {
        super.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * Set key-value pair with Duration-based expiration.
     */
    public void set(@NonNull K key, @NonNull V value, @NonNull Duration duration) {
        super.opsForValue().set(key, value, duration);
    }

    /**
     * Set value only if key does not exist.

     */
    public Boolean setIfAbsent(@NonNull K key, @NonNull V value) {
        return super.opsForValue().setIfAbsent(key, value);
    }

    /**
     * Set value with expiration only if key does not exist.
     */
    public Boolean setIfAbsent(@NonNull K key, @NonNull V value, @NonNull Duration duration) {
        return super.opsForValue().setIfAbsent(key, value, duration);
    }

    /**
     * Delete key.
     */
    public Boolean remove(@NonNull K key) {
        return super.delete(key);
    }

    /**
     * Delete multiple keys.
     */
    public Long remove(@NonNull Collection<K> keys) {
        return super.delete(keys);
    }

    /**
     * Check if key exists.
     */
    public Boolean exists(@NonNull K key) {
        return super.hasKey(key);
    }

    /**
     * Set expiration for existing key.
     */
    public Boolean setExpire(@NonNull K key, long timeout, @NonNull TimeUnit timeUnit) {
        return super.expire(key, timeout, timeUnit);
    }

    /**
     * Set expiration for existing key using Duration.
     */
    public Boolean setExpire(@NonNull K key, @NonNull Duration duration) {
        return super.expire(key, duration);
    }

    /**
     * Get remaining time to live for key.
     */
    public Long getExpire(@NonNull K key, @NonNull TimeUnit timeUnit) {
        return super.getExpire(key, timeUnit);
    }

    /**
     * Increment numeric value.
     */
    public Long increment(@NonNull K key, long delta) {
        return super.opsForValue().increment(key, delta);
    }

    /**
     * Increment by 1.
     */
    public Long increment(@NonNull K key) {
        return increment(key, 1L);
    }

    /**
     * Decrement numeric value.
     */
    public Long decrement(@NonNull K key, long delta) {
        return super.opsForValue().decrement(key, delta);
    }

    /**
     * Decrement by 1.
     */
    public Long decrement(@NonNull K key) {
        return decrement(key, 1L);
    }

    /**
     * Get multiple values by keys.
     */
    @NonNull
    public List<V> multiGet(@NonNull Collection<K> keys) {
        List<V> values = super.opsForValue().multiGet(keys);
        return values != null ? values : Collections.emptyList();
    }

    /**
     * Get value and refresh expiration.
     */
    @Nullable
    public V getAndRefresh(@NonNull K key, @NonNull Duration duration) {
        V value = get(key);
        if (value != null) {
            setExpire(key, duration);
        }
        return value;
    }

    /**
     * Get value and refresh expiration, returning Optional.
     */
    @NonNull
    public Optional<V> getAndRefreshOptional(@NonNull K key, @NonNull Duration duration) {
        return Optional.ofNullable(getAndRefresh(key, duration));
    }
}