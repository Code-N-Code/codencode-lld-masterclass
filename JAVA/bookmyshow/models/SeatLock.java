package bookmyshow.models;

import java.time.Instant;

public class SeatLock {
    private final String seatId;
    private final String showId;
    private final String lockedByUserId;
    private final Instant lockedAt;
    private final long timeoutInSeconds;

    public SeatLock(String seatId, String showId, String lockedByUserId, Instant lockedAt, long timeoutInSeconds) {
        this.seatId = seatId;
        this.showId = showId;
        this.lockedByUserId = lockedByUserId;
        this.lockedAt = lockedAt;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public boolean isExpired() {
        Instant expirationTime = lockedAt.plusSeconds(timeoutInSeconds);
        return Instant.now().isAfter(expirationTime);
    }

    public String getSeatId() { return seatId; }

    public String getShowId() { return showId; }

    public String getLockedByUserId() {
        return lockedByUserId;
    }

    public Instant getLockedAt() {
        return lockedAt;
    }

    public long getTimeoutInSeconds() {
        return timeoutInSeconds;
    }
}
