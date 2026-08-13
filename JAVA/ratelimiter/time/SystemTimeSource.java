package ratelimiter.time;

public final class SystemTimeSource implements TimeSource {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
