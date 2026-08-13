package ratelimiter.strategy;

import ratelimiter.model.RateLimitConfig;
import ratelimiter.model.WindowCounter;
import ratelimiter.time.TimeSource;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimitConfig config;
    private final TimeSource timeSource;

    private final ConcurrentHashMap<String, WindowCounter> counters =
            new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(
            RateLimitConfig config,
            TimeSource timeSource) {

        this.config = Objects.requireNonNull(config);
        this.timeSource = Objects.requireNonNull(timeSource);
    }

    @Override
    public boolean allow(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("clientId must not be blank");
        }

        long now = timeSource.currentTimeMillis();
        long currentWindowStart = getWindowStart(now);

        AtomicBoolean allowed = new AtomicBoolean(false);

        counters.compute(clientId, (key, existingCounter) -> {
            WindowCounter currentCounter = existingCounter;

            // First request for client, or request belongs to a new window.
            if (currentCounter == null
                    || currentCounter.getWindowStartMillis() != currentWindowStart) {

                allowed.set(true);
                return new WindowCounter(currentWindowStart, 1);
            }

            // Limit is already exhausted in this window.
            if (currentCounter.getRequestCount() >= config.getMaxRequests()) {
                allowed.set(false);
                return currentCounter;
            }

            // Consume one request atomically.
            allowed.set(true);
            return new WindowCounter(
                    currentCounter.getWindowStartMillis(),
                    currentCounter.getRequestCount() + 1
            );
        });

        return allowed.get();
    }

    private long getWindowStart(long currentTimeMillis) {
        return (currentTimeMillis / config.getWindowMillis())
                * config.getWindowMillis();
    }
}
