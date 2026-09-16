package bookmyshow.providers;

import bookmyshow.models.Seat;
import bookmyshow.models.Show;
import bookmyshow.models.User;

import java.util.List;

public interface SeatLockProvider {
    void lockSeats(Show show, List<Seat> seats, User user);
    void unlockSeats(Show show, List<Seat> seats, User user);
    boolean validateLock(Show show, Seat seat, User user);
}
