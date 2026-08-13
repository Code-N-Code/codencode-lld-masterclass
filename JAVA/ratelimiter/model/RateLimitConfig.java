package ratelimiter.model;

public class RateLimitConfig {
    private final int maxRequests;
    private final long windowMillis;

    public RateLimitConfig(int maxRequests, long windowMillis) {
        if (maxRequests <= 0) {
            throw new IllegalArgumentException("maxRequests must be positive");
        }

        if (windowMillis <= 0) {
            throw new IllegalArgumentException("windowMillis must be positive");
        }

        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowMillis() {
        return windowMillis;
    }
}
