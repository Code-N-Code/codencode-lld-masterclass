package ratelimiter.strategy;

public interface RateLimiter {
    boolean allow(String clientId);
}
