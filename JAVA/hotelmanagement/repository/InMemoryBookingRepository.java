package hotelmanagement.repository;

import hotelmanagement.model.Booking;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookingRepository implements BookingRepository {

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    @Override
    public void save(Booking booking) {
        bookings.put(booking.getId(), booking);
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }
}