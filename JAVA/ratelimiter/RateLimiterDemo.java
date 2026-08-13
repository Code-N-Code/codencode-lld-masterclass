package ratelimiter;

import ratelimiter.model.RateLimitConfig;
import ratelimiter.service.RateLimitService;
import ratelimiter.strategy.FixedWindowRateLimiter;
import ratelimiter.time.SystemTimeSource;
import ratelimiter.time.TimeSource;

public class RateLimiterDemo {
     static void main(String[] args) throws InterruptedException {
        RateLimitConfig config = new RateLimitConfig(1, 1_000);
        TimeSource timeSource = new SystemTimeSource();
        RateLimitService rateLimitService = new RateLimitService(new FixedWindowRateLimiter(config, timeSource));

        boolean isAllowed = rateLimitService.isRequestAllowed("C1");
        System.out.println(isAllowed);

        Thread.sleep(700);
        isAllowed = rateLimitService.isRequestAllowed("C1");
        System.out.println(isAllowed);
    }
}
