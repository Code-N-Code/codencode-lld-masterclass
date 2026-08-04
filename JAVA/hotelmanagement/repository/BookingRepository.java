package hotelmanagement.repository;

import hotelmanagement.domain.model.Booking;

import java.util.Optional;

public interface BookingRepository {

    void save(Booking booking);

    Optional<Booking> findById(String bookingId);
}