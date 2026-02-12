package com.autowhouse.loginservice.service.redis;

import java.util.Optional;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
public interface RateLimiterService {
    Optional<Integer> getTries(String email);

    void setTries(String email, int tryNumber);

    void onSuccessRemove(String email, int tryNumber);
}
