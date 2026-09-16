package bookmyshow.providers;

import bookmyshow.exceptions.SeatUnavailableException;
import bookmyshow.models.Seat;
import bookmyshow.models.SeatLock;
import bookmyshow.models.Show;
import bookmyshow.models.User;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySeatLockProvider implements SeatLockProvider {
    private final long lockTimeoutInSeconds;

    // Outer Map key: Show ID, Inner Map key: Seat ID
    private final Map<String, Map<String, SeatLock>> locks = new ConcurrentHashMap<>();

    // Fine-grained locks map: dedicated monitor object for each showId
    private final Map<String, Object> showMonitors = new ConcurrentHashMap<>();

    public InMemorySeatLockProvider(long lockTimeoutInSeconds) {
        this.lockTimeoutInSeconds = lockTimeoutInSeconds;
    }

    private Object getShowMonitor(String showId) {
        return showMonitors.computeIfAbsent(showId, id -> new Object());
    }

    @Override
    public void lockSeats(Show show, List<Seat> seats, User user) {
        synchronized (getShowMonitor(show.getId())) {
            locks.putIfAbsent(show.getId(), new ConcurrentHashMap<>());
            Map<String, SeatLock> showLocks = locks.get(show.getId());

            // Step 1: Pre-validation - check if any seat is already locked and valid
            for (Seat seat : seats) {
                if (isSeatLocked(showLocks, seat, user)) {
                    throw new SeatUnavailableException("Seat " + seat.getId() + " is temporarily locked by another user.");
                }
            }

            // Step 2: Atomic lock acquisition
            Instant now = Instant.now();
            for (Seat seat : seats) {
                SeatLock lock = new SeatLock(seat.getId(), show.getId(), user.getId(), now, lockTimeoutInSeconds);
                showLocks.put(seat.getId(), lock);
            }
        }
    }

    @Override
    public void unlockSeats(Show show, List<Seat> seats, User user) {
        synchronized (getShowMonitor(show.getId())) {
            Map<String, SeatLock> showLocks = locks.get(show.getId());
            if (showLocks == null) {
                return;
            }

            for (Seat seat : seats) {
                SeatLock existingLock = showLocks.get(seat.getId());
                if (existingLock != null && existingLock.getLockedByUserId().equals(user.getId())) {
                    showLocks.remove(seat.getId());
                }
            }
        }
    }

    @Override
    public boolean validateLock(Show show, Seat seat, User user) {
        synchronized (getShowMonitor(show.getId())) {
            Map<String, SeatLock> showLocks = locks.get(show.getId());
            if (showLocks == null) {
                return false;
            }

            SeatLock lock = showLocks.get(seat.getId());
            return lock != null && !lock.isExpired() && lock.getLockedByUserId().equals(user.getId());
        }
    }

    private boolean isSeatLocked(Map<String, SeatLock> showLocks, Seat seat, User requestingUser) {
        SeatLock lock = showLocks.get(seat.getId());
        if (lock == null) {
            return false;
        }
        if (lock.isExpired()) {
            showLocks.remove(seat.getId());
            return false;
        }
        return !lock.getLockedByUserId().equals(requestingUser.getId());
    }
}