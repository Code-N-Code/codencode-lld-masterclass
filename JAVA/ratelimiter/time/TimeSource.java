package ratelimiter.time;

public interface TimeSource {
    long currentTimeMillis();
}
