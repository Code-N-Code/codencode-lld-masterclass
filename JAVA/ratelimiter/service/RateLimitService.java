package ratelimiter.service;

import ratelimiter.strategy.RateLimiter;

import java.util.Objects;

public final class RateLimitService {

    private final RateLimiter rateLimiter;

    public RateLimitService(RateLimiter rateLimiter) {
        this.rateLimiter = Objects.requireNonNull(rateLimiter);
    }

    public boolean isRequestAllowed(String clientId) {
        return rateLimiter.allow(clientId);
    }
}
