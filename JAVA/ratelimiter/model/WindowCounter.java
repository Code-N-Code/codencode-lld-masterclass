package ratelimiter.model;

public class WindowCounter {
    private final long windowStartMillis;
    private final int requestCount;

    public WindowCounter(long windowStartMillis, int requestCount) {
        this.windowStartMillis = windowStartMillis;
        this.requestCount = requestCount;
    }

    public long getWindowStartMillis() {
        return windowStartMillis;
    }

    public int getRequestCount() {
        return requestCount;
    }
}
